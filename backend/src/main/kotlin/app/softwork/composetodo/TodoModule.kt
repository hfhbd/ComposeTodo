package app.softwork.composetodo

import app.softwork.cloudkitclient.*
import app.softwork.composetodo.controller.*
import app.softwork.composetodo.dto.*
import io.ktor.client.*
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.resources.Resources
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlin.time.Duration.Companion.days

fun Application.TodoModule(db: Client.Database, oatuhClient: HttpClient) {
    install(Resources)
    install(ContentNegotiation) {
        json()
        ignoreType<String>()
    }

    val userController = UserController(db = db)
    val todoController = TodoController(db = db)

    install(Authentication) {
        basic("login") {
            validate { (name, password) ->
                userController.findBy(name, password)
            }
        }
        oauth("google") {
            urlProvider = { "http://localhost:8080/callback" }
            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = "google",
                    authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
                    accessTokenUrl = "https://accounts.google.com/o/oauth2/token",
                    requestMethod = HttpMethod.Post,
                    clientId = System.getenv("GOOGLE_CLIENT_ID"),
                    clientSecret = System.getenv("GOOGLE_CLIENT_SECRET"),
                    defaultScopes = listOf("https://www.googleapis.com/auth/userinfo.profile"),
                    extraAuthParameters = listOf("access_type" to "offline"),
                    onStateCreated = { call, state ->
                      //  redirects[state] = call.request.queryParameters["redirectUrl"]!!
                    }
                )
            }
            client = oatuhClient
        }
        oauth("apple") {
            urlProvider = { "http://localhost:8080/callback" }
            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = "google",
                    authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
                    accessTokenUrl = "https://accounts.google.com/o/oauth2/token",
                    requestMethod = HttpMethod.Post,
                    clientId = System.getenv("GOOGLE_CLIENT_ID"),
                    clientSecret = System.getenv("GOOGLE_CLIENT_SECRET"),
                    defaultScopes = listOf("https://www.googleapis.com/auth/userinfo.profile"),
                    extraAuthParameters = listOf("access_type" to "offline"),
                    onStateCreated = { call, state ->
                     //   redirects[state] = call.request.queryParameters["redirectUrl"]!!
                    }
                )
            }
            client = oatuhClient
        }
    }

    install(Sessions) {
        cookie("SESSION", RefreshToken.serializer(), Json) {
            path = "/refreshToken"
            httpOnly = true
            extensions["SameSite"] = "strict"
            maxAgeInSeconds = 1.days.inWholeSeconds
            secure = true
        }
    }

    routing {
        get<Online, String> {
            "API is online"
        }

        post<API.Users, User.New, Token> { _, newUser ->
            if (newUser.username.isEmpty()) {
                throw BadRequestException("Empty username")
            }
            if (newUser.password.isEmpty()) {
                throw BadRequestException("Empty password")
            }
            if (newUser.password != newUser.passwordAgain) {
                throw BadRequestException("password was not equal to passwordAgain")
            }
            // userController.createUser(jwtProvider, newUser.toDAO())
            TODO("")
        }

        authenticate("login") {
            post<API.RefreshToken, Token> {
                val longRefreshToken = RefreshToken(user.recordName)
                call.sessions.set(longRefreshToken)
                TODO() // jwtProvider.token(longRefreshToken)
            }

            get<API.RefreshToken, Token> {
                val refreshToken: RefreshToken = call.sessions.get() ?: throw BadRequestException("Token is missing")

                TODO() // jwtProvider.token(refreshToken)
            }
        }

        authenticate {
            delete<API.RefreshToken, HttpStatusCode> {
                call.sessions.clear<RefreshToken>()
                HttpStatusCode.OK
            }

            get<API.Me, User> {
                userController.find(user.recordName)?.toDTO() ?: throw NotFoundException()
            }
            put<API.Me, User, User> { _, toUpdate ->
                val dao = toUpdate.toDAO()
                if (user.recordName != dao.recordName) {
                    throw BadRequestException("Wrong username")
                }
                userController.update(dao).toDTO()
            }
            delete<API.Me, HttpStatusCode> {
                with(call) {
                    todoController.deleteAll(user)
                    userController.delete(user)
                    HttpStatusCode.OK
                }
            }

            get<API.Todos, List<TodoDTO>> {
                todoController.todos(user)
            }
            post<API.Todos, TodoDTO, TodoDTO> { _, newTodo ->
                todoController.create(newTodo.toDAO(user)).toDTO()
            }

            get<API.Todos.Id, TodoDTO> {
                todoController.getTodo(user, it.id)?.toDTO() ?: throw NotFoundException()
            }
            put<API.Todos.Id, TodoDTO, TodoDTO> { parameters, toUpdate ->
                todoController.update(user, parameters.id, toUpdate)?.toDTO() ?: throw NotFoundException()
            }
            delete<API.Todos.Id, HttpStatusCode> {
                with(call) {
                    todoController.delete(user, it.id) ?: throw NotFoundException()
                    HttpStatusCode.OK
                }
            }
        }
    }
}

@Serializable
@Resource("/")
class Online
