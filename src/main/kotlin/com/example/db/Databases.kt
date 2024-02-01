package com.example.db

import com.example.db.models.*
import com.example.db.routing.userRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

const val DB_NAME = "ISKRA_DB"

fun Application.configureDatabases() {

    val database = Database.connect(
        url = "jdbc:mysql://@localhost:3306/$DB_NAME?createDatabaseIfNotExist=true",
        driver = "com.mysql.cj.jdbc.Driver",
        user = "admin",
        password = "1590",
    )

    createTables(database)

    val userController = UserController()

    routing {
        userRouting(userController)
    }
}

fun createTables(db: Database) {
    transaction(db) {
        SchemaUtils.create(UserTable)

        SchemaUtils.create(StaffTable)
        SchemaUtils.create(PostTable)

        SchemaUtils.create(TaskTable)
        SchemaUtils.create(QuestionTaskTable)
        SchemaUtils.create(CompoundTaskTable)
        SchemaUtils.create(UserTaskTable)

        SchemaUtils.create(AchievementTable)
        SchemaUtils.create(UserAchievementTable)

        SchemaUtils.create(QuestionTable)

        SchemaUtils.create(MapMarkTable)
    }
}
