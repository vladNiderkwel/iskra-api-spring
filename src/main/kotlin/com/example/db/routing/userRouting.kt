package com.example.db.routing

import com.example.db.models.User
import com.example.db.models.UserController
import com.example.db.models.UserEntity
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Routing.userRouting(userController: UserController) {

    route("/user") {

        get {
            call.respondRedirect("/user/")
        }

        get("/") {
            call.respond(
                status = HttpStatusCode.OK,
                message = transaction {
                    UserEntity.all().map { it.toUser() }
                }
            )

            call.respond(HttpStatusCode.OK, userController.all())
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Неверный ID")

            val user = userController.find(id)
            user?.let {
                call.respond(HttpStatusCode.OK, it)
            } ?: call.respond(HttpStatusCode.NotFound)
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Неверный ID")

            userController.delete(id)
            //call.respond(HttpStatusCode.OK, isDeleted)
        }

        put("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Неверный ID")
            val user = call.receive<User>()

            userController.update(id, user)
            //call.respond(HttpStatusCode.OK, isUpdated)
        }

        post("/") {
            val user = call.receive<User>()

            val id = userController.create(user)
            call.respond(HttpStatusCode.Created, id)

        }
    }
}