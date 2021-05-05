package app.softwork.composetodo

import app.softwork.composetodo.controller.*
import app.softwork.composetodo.definitions.*
import app.softwork.composetodo.dto.*
import com.auth0.jwt.*
import com.auth0.jwt.algorithms.*
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import kotlinx.datetime.*
import kotlinx.uuid.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import kotlin.time.*

@ExperimentalTime
fun Application.TodoModule(db: Database, jwtProvider: JWTProvider) {

    install(Authentication) {
        basic("login") {
            validate { (name, password) ->
                UserController.findBy(name, password)
            }
        }
        jwt {
            verifier {
                jwtProvider.verifier
            }
            validate { credentials ->
                jwtProvider.validate(credentials)
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

    transaction {
        SchemaUtils.create(Users, Todos)
    }

    routing {
        get {
            call.respondText { "API is online" }
        }

        post("/users") {
            call.respondJson(Token.serializer()) {
                val newUser = body(User.New.serializer())
                UserController.createUser(jwtProvider, newUser)
            }
        }

        authenticate("login") {
            get("/refreshToken") {
                call.respondJson(Token.serializer()) {
                    val user = call.principal<app.softwork.composetodo.dao.User>()!!
                    call.sessions.set(RefreshToken(user.id.toString()))
                    jwtProvider.token(user)
                }
            }
        }
        authenticate {
            route("/refreshToken") {
                delete {
                    call.sessions.clear<RefreshToken>()
                    call.respond(HttpStatusCode.OK)
                }
            }

            route("/me") {
                get {
                    call.respondJson(User.serializer()) {
                        call.principal<app.softwork.composetodo.dao.User>()!!.toDTO()
                    }
                }
                put {
                    call.respondJson(User.serializer()) {
                        val user = call.principal<app.softwork.composetodo.dao.User>()!!
                        val toUpdate = body(User.serializer())
                        UserController(user).update(toUpdate)
                    }
                }
                delete {
                    with(call) {
                        val user = call.principal<app.softwork.composetodo.dao.User>()!!
                        TodoController(user).deleteAll()
                        UserController(user).delete()
                        respond(HttpStatusCode.OK)
                    }
                }
            }

            route("/todos") {
                get {
                    call.respondJsonList(Todo.serializer()) {
                        val user = call.principal<app.softwork.composetodo.dao.User>()!!
                        TodoController(user).todos()
                    }
                }
                post {
                    call.respondJson(Todo.serializer()) {
                        val user = call.principal<app.softwork.composetodo.dao.User>()!!
                        val newTodo = body(Todo.serializer())
                        TodoController(user).create(newTodo)
                    }
                }

                route("/{todoID}") {
                    get {
                        call.respondJson(Todo.serializer()) {
                            val user = call.principal<app.softwork.composetodo.dao.User>()!!
                            val todoID: UUID by parameters
                            TodoController(user).getTodo(todoID)
                        }
                    }
                    put {
                        call.respondJson(Todo.serializer()) {
                            val user = call.principal<app.softwork.composetodo.dao.User>()!!
                            val todoID: UUID by parameters
                            val toUpdate = body(Todo.serializer())
                            TodoController(user).update(todoID, toUpdate)
                        }
                    }
                    delete {
                        with(call) {
                            val user = call.principal<app.softwork.composetodo.dao.User>()!!
                            val todoID: UUID by parameters
                            TodoController(user).delete(todoID)
                            respond(HttpStatusCode.OK)
                        }
                    }
                }
            }
        }
    }
}
