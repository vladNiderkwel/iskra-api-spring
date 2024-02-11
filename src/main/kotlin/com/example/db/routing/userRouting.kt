package com.example.db.routing

import com.example.db.INVALID_ID_FORMAT
import com.example.db.models.User
import com.example.db.models.UserController
import com.example.db.models.UserEntity
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Routing.userRouting(controller: UserController) {

    route("/user") {

        get("/") {
            call.respond(
                status = HttpStatusCode.OK,
                message = transaction {
                    UserEntity.all().map { it.toUser() }
                }
            )

            call.respond(HttpStatusCode.OK, controller.all())
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toInt()

            id?.let {
                val user = controller.find(id)
                user?.let {
                    call.respond(HttpStatusCode.OK, it)
                } ?: call.respond(HttpStatusCode.NotFound)

            } ?: call.respond(HttpStatusCode.BadRequest, INVALID_ID_FORMAT)
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toInt()

            id?.let {
                controller.delete(id)

            } ?: call.respond(HttpStatusCode.BadRequest, INVALID_ID_FORMAT)
        }

        put("/{id}") {
            val id = call.parameters["id"]?.toInt()

            id?.let {
                val user = call.receive<User>()

                controller.update(id, user)
            } ?: call.respond(HttpStatusCode.BadRequest, INVALID_ID_FORMAT)
        }

        post("/") {
            val user = call.receive<User>()
            val id = controller.create(user)

            call.respond(HttpStatusCode.Created, id)
        }
    }
}