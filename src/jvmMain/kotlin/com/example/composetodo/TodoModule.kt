package com.example.composetodo

import com.example.composetodo.controller.TodoController
import com.example.composetodo.controller.UserController
import com.example.composetodo.definitions.Users
import com.example.composetodo.dto.Todo
import com.example.composetodo.dto.User
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
        SchemaUtils.create(Users, com.example.composetodo.definitions.Todos)
    }

    routing {
        get {
            call.respondText { "API is online" }
        }
        route("/users") {
            get {
                val users = UserController.allUsers()
                call.respondJson(User.serializer(), users)
            }
            post {
                val newUser = call.body(User.serializer())
                val newUserCreated = UserController.createUser(newUser)
                call.respondJson(User.serializer(), newUserCreated)
            }

            route("/{userID}") {
                get {
                    val userID: UUID by call.parameters
                    val user = UserController(userID).getUser()
                    call.respondJson(User.serializer(), user)
                }
                put {
                    val userID: UUID by call.parameters
                    val toUpdate = call.body(User.serializer())
                    val updated = UserController(userID).update(toUpdate)
                    call.respondJson(User.serializer(), updated)
                }
                delete {
                    val userID: UUID by call.parameters
                    UserController(userID).delete()
                    call.response.status(HttpStatusCode.OK)
                }



                route("/todos") {
                    get {
                        val userID: UUID by call.parameters
                        val todos = TodoController(userID).todos()
                        call.respondJson(Todo.serializer(), todos)
                    }
                    post {
                        val userID: UUID by call.parameters
                        val newTodo = call.body(Todo.serializer())
                        val created = TodoController(userID).create(newTodo)
                        call.respondJson(Todo.serializer(), created)
                    }

                    route("/todoID") {
                        get("/todoID") {
                            val userID: UUID by call.parameters
                            val todoID: UUID by call.parameters
                            val todo = TodoController(userID).getTodo(todoID)
                            call.respondJson(Todo.serializer(), todo)
                        }
                        put {
                            val userID: UUID by call.parameters
                            val todoID: UUID by call.parameters
                            val toUpdate = call.body(Todo.serializer())
                            val created = TodoController(userID).update(todoID, toUpdate)
                            call.respondJson(Todo.serializer(), created)
                        }
                        delete {
                            val userID: UUID by call.parameters
                            val todoID: UUID by call.parameters
                            TodoController(userID).delete(todoID)
                            call.response.status(HttpStatusCode.OK)
                        }
                    }
                }
            }
        }
    }
}