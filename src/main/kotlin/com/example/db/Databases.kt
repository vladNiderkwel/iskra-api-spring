package com.example.db

import com.example.db.models.UserService
import com.example.db.routing.userRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*

const val DB_NAME = "iskra_db"

fun Application.configureDatabases() {

    val database = Database.connect(
        url = "jdbc:mysql://@localhost:3306/$DB_NAME?createDatabaseIfNotExist=true",
        user = "admin",
        driver = "com.mysql.cj.jdbc.Driver",
        password = "WaterBridge159",
    )

    val userService = UserService(database)

    routing {
        userRouting(userService)
    }
}
