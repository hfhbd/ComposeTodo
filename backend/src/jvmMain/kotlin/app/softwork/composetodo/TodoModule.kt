package app.softwork.composetodo

import app.softwork.composetodo.controller.TodoController
import app.softwork.composetodo.controller.UserController
import app.softwork.composetodo.definitions.Todos
import app.softwork.composetodo.definitions.Users
import app.softwork.composetodo.dto.Todo
import app.softwork.composetodo.dto.User
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.uuid.UUID
import kotlinx.uuid.ktor.uuid
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.TodoModule() {
    install(DataConversion) {
        uuid()
    }

    transaction {
        SchemaUtils.create(Users, Todos)
    }

    routing {
        get {
            call.respondText { "API is online" }
        }
        route("/users") {
            post {
                call.respondJson(User.serializer()) {
                    val newUser = body(User.serializer())
                    UserController.createUser(newUser)
                }
            }

            route("/{userID}") {
                get {
                    call.respondJson(User.serializer()) {
                        val userID: UUID by parameters
                        UserController(userID).getUser()
                    }
                }
                put {
                    call.respondJson(User.serializer()) {
                        val userID: UUID by parameters
                        val toUpdate = body(User.serializer())
                        UserController(userID).update(toUpdate)
                    }
                }
                delete {
                    with(call) {
                        val userID: UUID by parameters
                        UserController(userID).delete()
                        respond(HttpStatusCode.OK)
                    }
                }

                route("/todos") {
                    get {
                        call.respondJsonList(Todo.serializer()) {
                            val userID: UUID by parameters
                            TodoController(userID).todos()
                        }
                    }
                    post {
                        call.respondJson(Todo.serializer()) {
                            val userID: UUID by parameters
                            val newTodo = body(Todo.serializer())
                            TodoController(userID).create(newTodo)
                        }
                    }

                    route("/todoID") {
                        get("/{todoID}") {
                            call.respondJson(Todo.serializer()) {
                                val userID: UUID by parameters
                                val todoID: UUID by parameters
                                TodoController(userID).getTodo(todoID)
                            }
                        }
                        put {
                            call.respondJson(Todo.serializer()) {
                                val userID: UUID by parameters
                                val todoID: UUID by parameters
                                val toUpdate = body(Todo.serializer())
                                TodoController(userID).update(todoID, toUpdate)
                            }
                        }
                        delete {
                            with(call) {
                                val userID: UUID by parameters
                                val todoID: UUID by parameters
                                TodoController(userID).delete(todoID)
                                respond(HttpStatusCode.OK)
                            }
                        }
                    }
                }
            }
        }
    }
}
