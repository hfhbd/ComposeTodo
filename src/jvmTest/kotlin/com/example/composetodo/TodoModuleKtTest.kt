package com.example.composetodo

import com.example.composetodo.dto.User
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.uuid.UUID
import kotlinx.uuid.generateUUID
import org.jetbrains.exposed.sql.Database
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals


internal class TodoModuleKtTest {
    @BeforeTest
    fun initDB() {
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;", "org.h2.Driver")
    }

    @Test
    fun newUser() = withTestApplication({
        TodoModule()
    }) {


        with(handleRequest(HttpMethod.Get, "/users").response) {
            assertEquals(HttpStatusCode.OK, status()!!)
            assertEqualsJsonBody(emptyList(), User.serializer())
        }

        val newUser =
            User(id = UUID.generateUUID(Random.Default), firstName = "User 1", lastName = "Doe")

        with(handleRequest(HttpMethod.Post, "/users") {
            setBody(newUser, User.serializer())
        }.response) {
            assertEquals(HttpStatusCode.OK, status()!!)
            assertEqualsJsonBody(newUser, User.serializer())
        }

        with(handleRequest(HttpMethod.Get, "/users").response) {
            assertEquals(HttpStatusCode.OK, status()!!)
            assertEqualsJsonBody(listOf(newUser), User.serializer())
        }
    }

    @Test
    fun online() = withTestApplication({
        TodoModule()
    }) {
        with(handleRequest(HttpMethod.Get, "/").response) {
            assertEquals(HttpStatusCode.OK, status()!!)
            assertEquals("API is online", content!!)
        }
    }
}