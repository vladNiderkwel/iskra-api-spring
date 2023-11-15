package com.example.db

import com.example.DB_NAME
import com.example.db.models.*
import com.example.db.routing.postRouting
import com.example.db.routing.userRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {

    val database = Database.connect(
        url = "jdbc:mysql://@localhost:3306/$DB_NAME?createDatabaseIfNotExist=true",
        user = "admin",
        driver = "com.mysql.cj.jdbc.Driver",
        password = "WaterBridge159",
    )

    createTables(database)

    routing {
        userRouting()
        postRouting()
    }
}

fun createTables(db: Database) {
    transaction(db) {
        SchemaUtils.create(UserTable)
        SchemaUtils.create(PostTable)
    }
}
