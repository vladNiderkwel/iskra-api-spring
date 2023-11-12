package com.example.db.routing

import com.example.db.models.User
import com.example.db.models.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.userRouting(userService: UserService) {
    route("/user") {

        get {
            call.respondRedirect("/user/")
        }

        get("/") {
            val allUser = userService.all()
            call.respond(HttpStatusCode.OK, allUser)
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Неверный ID")
            val user = userService.find(id)
            user?.let {
                call.respond(HttpStatusCode.OK, it)
            } ?: call.respond(HttpStatusCode.NotFound)
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Неверный ID")

            val isDeleted = userService.delete(id)
            call.respond(HttpStatusCode.OK, isDeleted)
        }

        put("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Неверный ID")
            val user = call.receive<User>()

            val isUpdated = userService.update(id, user)
            call.respond(HttpStatusCode.OK, isUpdated)
        }

        post("/") {
            val user = call.receive<User>()
            val id = userService.create(user)
            call.respond(HttpStatusCode.Created, id)
        }
    }
}