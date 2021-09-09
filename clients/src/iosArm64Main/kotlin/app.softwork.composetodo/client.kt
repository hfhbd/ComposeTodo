package app.softwork.composetodo

import app.softwork.composetodo.dto.*
import io.ktor.client.*
import io.ktor.client.engine.ios.*
import io.ktor.client.features.*
import io.ktor.client.features.cookies.*
import io.ktor.http.*
import io.ktor.utils.io.errors.*
import kotlinx.cinterop.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.datetime.*
import kotlinx.uuid.*
import platform.CoreData.*
import platform.Foundation.*
import kotlin.coroutines.*
import kotlin.properties.*
import kotlin.reflect.*

fun api(cookiesStorage: CookiesStorage) = API.LoggedOut(HttpClient(Ios) {
    install(HttpCookies) {
        storage = cookiesStorage
    }
    install(DefaultRequest) {
        url {
            protocol = URLProtocol.HTTPS
            host = "api.todo.softwork.app"
        }
    }
})

class UserDefaultsCookieStorage : CookiesStorage {
    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        NSUserDefaults.standardUserDefaults.setValue(cookie.value, forKey = "refreshToken")
    }

    override fun close() {}

    override suspend fun get(requestUrl: Url): List<Cookie> =
        NSUserDefaults.standardUserDefaults.stringForKey("refreshToken")?.let {
            listOf(Cookie(name = "SESSION", value = it, secure = true, httpOnly = true))
        } ?: emptyList()
}

enum class LoginResult {
    Success, WrongPassword, NetworkError, UnknownError
}

sealed class RequestResult<T> {
    class Success<T>(val response: T) : RequestResult<T>()
    class NetworkException<T> : RequestResult<T>()
}

sealed class LoginState {
    object LoggedOut : LoginState()
    object LoggedIn : LoginState()
}

class ViewModel(
    scope: CoroutineScope = MainScope(),
    private val context: NSManagedObjectContext = container.viewContext,
    private var api: API = api(UserDefaultsCookieStorage())
) : CoroutineScope by scope {
    private var cachedData = emptyMap<UUID, TodoItem>()

    private val _loginState: MutableStateFlow<LoginState> = MutableStateFlow(LoginState.LoggedOut)
    val loginState: Flow<LoginState> = _loginState
    private val _data: MutableStateFlow<RequestResult<List<Todo>>> =
        MutableStateFlow(RequestResult.Success(emptyList()))
    val data: Flow<RequestResult<List<Todo>>> = _data

    suspend fun refresh(): RequestResult<List<Todo>> {
        val loggedIn = api as API.LoggedIn
        return loggedIn.tryNetwork {
            val todos = getTodos()
            transaction(context) {
                todos.forEach {
                    val found = cachedData[it.id]
                    if (found == null) {
                        TodoItem(context, it)
                    } else {
                        found.apply {
                            this.finished = it.finished
                            this.title = it.title
                            this.until = it.until?.toNSDate()
                            this.recordChangeTag = it.recordChangeTag
                        }
                    }
                }
            }
            todos
        }.also {
            updateData()
        }
    }

    private fun updateData() {
        launch {
            val result: RequestResult<List<Todo>> = TodoItem.fetchRequest(context) { }.getOrThrow().let {
                RequestResult.Success(it.map { it.toTodo() })
            }
            _data.value = result
        }
    }

    suspend fun delete(todo: Todo): RequestResult<Unit> {
        transaction(context) {
            val toDelete = cachedData[todo.id]!!
            context.deleteObject(toDelete)
        }
        val loggedIn = api as API.LoggedIn
        return loggedIn.tryNetwork {
            deleteTodo(todo.id)
        }.also {
            updateData()
        }
    }

    suspend fun create(todo: Todo): RequestResult<Todo> {
        transaction(context) { TodoItem(context, todo) }
        val loggedIn = api as API.LoggedIn
        return loggedIn.tryNetwork {
            this.createTodo(todo)
        }.also {
            updateData()
        }
    }

    private inline fun <T : API, R> T.tryNetwork(block: T.() -> R): RequestResult<R> {
        return try {
            RequestResult.Success(block())
        } catch (e: IOException) {
            RequestResult.NetworkException()
        } catch (e: Exception) {
            error(e)
        }
    }

    suspend fun login(username: String, password: String): LoginResult {
        val loggedOut = api as API.LoggedOut
        api = try {
            loggedOut.login(username, password)
        } catch (e: IOException) {
            return LoginResult.NetworkError
        } catch (e: Exception) {
            return LoginResult.UnknownError
        } ?: return LoginResult.WrongPassword
        _loginState.value = LoginState.LoggedIn
        return LoginResult.Success
    }

    companion object {
        val container = createPersistenceContainer("composetodo", NSManagedObjectModel().apply {
            entities = listOf(TodoItem.description())
        }) {
            if (it.isFailure) {
                error(it)
            }
        }
    }
}

fun createPersistenceContainer(
    name: String,
    model: NSManagedObjectModel,
    inMemory: Boolean = false,
    block: (Result<NSPersistentStoreDescription>) -> Unit
): NSPersistentContainer {
    val container = NSPersistentContainer(name, model)
    if (inMemory) {
        (container.persistentStoreDescriptions as List<NSPersistentStoreDescription>).first().URL =
            NSURL.fileURLWithPath("/dev/null")
    }
    container.loadPersistentStoresWithCompletionHandler { nsPersistentStoreDescription, nsError ->
        if (nsError != null) {
            block(Result.failure(CDError(nsError)))
        } else {
            block(Result.success(nsPersistentStoreDescription!!))
        }
    }
    return container
}

class CDError(underlyingError: NSError) : Exception(underlyingError.localizedDescription)

suspend inline fun <R> transaction(
    context: NSManagedObjectContext,
    crossinline block: suspend NSManagedObjectContext.() -> R
): R {
    val result = context.block()
    suspendCoroutine<Boolean> {
        it.resumeWith(withErrorPointer { context.save(it) })
    }
    return result
}

fun <R> withErrorPointer(block: (CPointer<ObjCObjectVar<NSError?>>) -> R): Result<R> = memScoped {
    val errorPointer: ObjCObjectVar<NSError?> = alloc()
    val pointer = errorPointer.ptr

    val result = block(pointer)
    val error = pointer.pointed.value
    return if (error != null) {
        Result.failure(CDError(error))
    } else {
        Result.success(result)
    }
}

class Attribute<T : Any>(val description: NSAttributeDescription) {
    operator fun getValue(ref: Any?, property: KProperty<*>): T {
        error("Dont call the schema to get an actual value. Use your model class instead.")
    }
}

class CDDescription(name: String, managedObjectClassName: String = name) : NSEntityDescription() {
    init {
        this.name = name
        this.managedObjectClassName = managedObjectClassName
    }

    fun uuid(name: String): Attribute<UUID> = add(name, NSUUIDAttributeType)
    fun string(name: String): Attribute<String> = add(name, NSStringAttributeType)
    fun instant(name: String): Attribute<Instant> = add(name, NSDateAttributeType)
    fun boolean(name: String): Attribute<Boolean> = add(name, NSBooleanAttributeType)

    private fun <T : Any> add(name: String, type: NSAttributeType): Attribute<T> {
        val new = NSAttributeDescription().apply {
            attributeType = type
            this.name = name
        }
        properties = properties + new
        return Attribute(new)
    }
}
/*
object Schema : CDDescription<app.softwork.composetodo.models.Todo>("TodoItem"), app.softwork.composetodo.models.Todo {
    override val id by uuid("id")
    override val title by string("title")
    override val until by instant("until")
    override val finished by boolean("finished")
    override val recordChangeTag by string("recordChangeTag")
}

abstract class ManagedObject<Domain>(
    description: CDDescription<Domain>,
    context: NSManagedObjectContext
) : NSManagedObject(description, context)

class TodoItem2(
    context: NSManagedObjectContext,
    val id: NSUUID,
    val title: String,
    val until: NSDate?,
    val finished: Boolean,
    val recordChangeTag: String?
) : ManagedObject<app.softwork.composetodo.models.Todo>(Schema, context)
*/
class TodoItem private constructor(
    context: NSManagedObjectContext,
    val id: NSUUID,
    var title: String,
    var until: NSDate?,
    var finished: Boolean,
    var recordChangeTag: String?
) : NSManagedObject(description(), context) {
    constructor(context: NSManagedObjectContext, todo: Todo) :
            this(
                context,
                id = todo.id.toNsUUID(),
                title = todo.title,
                until = todo.until?.toNSDate(),
                finished = todo.finished,
                recordChangeTag = todo.recordChangeTag
            )

    fun toTodo(): Todo = Todo(
        id.toKotlinUUID(),
        title = title,
        until = until?.toKotlinInstant(),
        finished = finished,
        recordChangeTag = recordChangeTag
    )

    companion object {

        suspend fun fetchRequest(
            context: NSManagedObjectContext,
            block: NSFetchRequest.() -> Unit
        ): Result<List<TodoItem>> =
            withErrorPointer {
                context.executeFetchRequest(NSFetchRequest(description().name!!).apply(block), it) as List<TodoItem>
            }

        fun description() = description("TodoItem") {
            uuid("id")
            string("title")
            instant("until")
            boolean("finished")
            string("recordChangeTag")
        }
    }
}

fun description(
    name: String,
    managedObjectClassName: String = name,
    block: CDDescription.() -> Unit
): NSEntityDescription =
    CDDescription(name, managedObjectClassName).apply(block)
