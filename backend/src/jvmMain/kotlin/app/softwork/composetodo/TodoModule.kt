package app.softwork.composetodo

import app.softwork.cloudkitclient.*
import app.softwork.composetodo.controller.*
import app.softwork.composetodo.dto.*
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import kotlinx.uuid.*
import kotlin.time.*

@ExperimentalTime
fun Application.TodoModule(db: Client.Database, jwtProvider: JWTProvider) {
    val userController = UserController(db = db)
    val todoController = TodoController(db = db)

    install(Authentication) {
        basic("login") {
            validate { (name, password) ->
                userController.findBy(name, password)
            }
        }
        jwt {
            verifier {
                jwtProvider.verifier
            }
            validate { credentials ->
                jwtProvider.validate(credentials) { userID ->
                    userController.find(userID)
                }
            }
        }
    }

    install(Sessions) {
        cookie<RefreshToken>("SESSION") {
            cookie.path = "/refreshToken"
            cookie.httpOnly = true
            cookie.extensions["SameSite"] = "strict"
            cookie.maxAgeInSeconds = 1.days.inSeconds.toLong()
        }
    }

    routing {
        get {
            call.respondText { "API is online" }
        }

        post("/users") {
            call.respondJson(Token.serializer()) {
                val newUser = body(User.New.serializer())
                require(newUser.password == newUser.passwordAgain)
                userController.createUser(jwtProvider, newUser.toDAO())
            }
        }

        route("/refreshToken") {

            authenticate("login") {
                post {
                    call.respondJson(Token.serializer()) {
                        val user = call.principal<app.softwork.composetodo.dao.User>()!!
                        val longRefreshToken = RefreshToken(user.recordName)
                        call.sessions.set(longRefreshToken)
                        jwtProvider.token(longRefreshToken)
                    }
                }
            }

            get {
                val refreshToken: RefreshToken? = call.sessions.get<RefreshToken>()
                if (refreshToken == null) {
                    call.respond(UnauthorizedResponse())
                } else {
                    call.respondJson(Token.serializer()) {
                        jwtProvider.token(refreshToken)
                    }
                }
            }

            authenticate {
                delete {
                    call.sessions.clear<RefreshToken>()
                    call.respond(HttpStatusCode.OK)
                }
            }
        }

        authenticate {
            route("/me") {
                get {
                    call.respondJson(User.serializer()) {
                        call.principal<app.softwork.composetodo.dao.User>()!!.toDTO()
                    }
                }
                put {
                    call.respondJson(User.serializer()) {
                        val toUpdate = body(User.serializer())
                        userController.update(toUpdate.toDAO()).toDTO()
                    }
                }
                delete {
                    with(call) {
                        val user = call.principal<app.softwork.composetodo.dao.User>()!!
                        todoController.deleteAll(user)
                        userController.delete(user)
                        respond(HttpStatusCode.OK)
                    }
                }
            }

            route("/todos") {
                get {
                    call.respondJsonList(Todo.serializer()) {
                        val user = call.principal<app.softwork.composetodo.dao.User>()!!
                        todoController.todos(user)
                    }
                }
                post {
                    call.respondJson(Todo.serializer()) {
                        val user = call.principal<app.softwork.composetodo.dao.User>()!!
                        val newTodo = body(Todo.serializer()).toDAO(user)
                        todoController.create(newTodo).toDTO()
                    }
                }

                route("/{todoID}") {
                    get {
                        call.respondJson(Todo.serializer()) {
                            val user = call.principal<app.softwork.composetodo.dao.User>()!!
                            val todoID: UUID by parameters
                            todoController.getTodo(user, todoID)?.toDTO()
                                ?: throw NotFoundException()
                        }
                    }
                    put {
                        call.respondJson(Todo.serializer()) {
                            val user = call.principal<app.softwork.composetodo.dao.User>()!!
                            val todoID: UUID by parameters
                            val toUpdate = body(Todo.serializer())
                            todoController.update(user, todoID, toUpdate)?.toDTO()
                                ?: throw NotFoundException()
                        }
                    }
                    delete {
                        with(call) {
                            val user = call.principal<app.softwork.composetodo.dao.User>()!!
                            val todoID: UUID by parameters
                            todoController.delete(user, todoID) ?: throw NotFoundException()
                            respond(HttpStatusCode.OK)
                        }
                    }
                }
            }
        }
    }
}
