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
import kotlin.time.Duration.Companion.days

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
            cookie.maxAgeInSeconds = 1.days.inWholeSeconds
            // cookie.secure = true // needs https://github.com/ktorio/ktor/pull/2754 to use this option in tests
        }
    }

    routing {
        get {
            call.respondText { "API is online" }
        }

        post("/users") {
            call.respondJson(Token.serializer()) {
                val newUser = body(User.New.serializer())
                if (newUser.username.isEmpty()) {
                    throw BadRequestException("Empty username")
                }
                if (newUser.password.isEmpty()) {
                    throw BadRequestException("Empty password")
                }
                if (newUser.password != newUser.passwordAgain) {
                    throw BadRequestException("password was not equal to passwordAgain")
                }
                userController.createUser(jwtProvider, newUser.toDAO())
            }
        }

        route("/refreshToken") {
            authenticate("login") {
                post {
                    call.respondJson(Token.serializer()) {
                        val longRefreshToken = RefreshToken(user.recordName)
                        call.sessions.set(longRefreshToken)
                        jwtProvider.token(longRefreshToken)
                    }
                }
            }

            get {
                val refreshToken: RefreshToken =
                    call.sessions.get<RefreshToken>() ?: throw BadRequestException("Token is missing")

                call.respondJson(Token.serializer()) {
                    jwtProvider.token(refreshToken)
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
                        userController.find(user.recordName)?.toDTO()
                            ?: throw NotFoundException()
                    }
                }
                put {
                    call.respondJson(User.serializer()) {
                        val toUpdate = body(User.serializer()).toDAO()
                        if (user.recordName != toUpdate.recordName) {
                            throw BadRequestException("Wrong username")
                        }
                        userController.update(toUpdate).toDTO()
                    }
                }
                delete {
                    with(call) {
                        todoController.deleteAll(user)
                        userController.delete(user)
                        respond(HttpStatusCode.OK)
                    }
                }
            }

            route("/todos") {
                get {
                    call.respondJsonList(TodoDTO.serializer()) {
                        todoController.todos(user)
                    }
                }
                post {
                    call.respondJson(TodoDTO.serializer()) {
                        val newTodo = body(TodoDTO.serializer()).toDAO(user)
                        todoController.create(newTodo).toDTO()
                    }
                }

                route("/{todoID}") {
                    get {
                        call.respondJson(TodoDTO.serializer()) {
                            val todoID by parameters
                            todoController.getTodo(user, todoID)?.toDTO()
                                ?: throw NotFoundException()
                        }
                    }
                    put {
                        call.respondJson(TodoDTO.serializer()) {
                            val todoID by parameters
                            val toUpdate = body(TodoDTO.serializer())
                            todoController.update(user, todoID, toUpdate)?.toDTO()
                                ?: throw NotFoundException()
                        }
                    }
                    delete {
                        with(call) {
                            val todoID by parameters
                            todoController.delete(user, todoID) ?: throw NotFoundException()
                            respond(HttpStatusCode.OK)
                        }
                    }
                }
            }
        }
    }
}
