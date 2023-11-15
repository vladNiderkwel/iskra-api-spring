package com.example.db.routing

import com.example.db.models.User
import com.example.db.models.UserEntity
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Routing.userRouting() {
    route("/user") {

        get {
            call.respondRedirect("/user/")
        }

        get("/") {
            call.respond(
                status = HttpStatusCode.OK,
                message = transaction {
                    UserEntity.all().map { it.model() }
                }
            )
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Неверный ID")
            val user = transaction {
                UserEntity.findById(id)
            }

            if (user == null) call.respond(HttpStatusCode.NotFound)
            else call.respond(HttpStatusCode.OK, it)
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Неверный ID")
            transaction {
                UserEntity.findById(id)?.delete()
            }
            call.respond(HttpStatusCode.OK)
        }

        put("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Неверный ID")
            val newData = call.receive<User>()

            val user = transaction {
                UserEntity.findById(id)
            }

            var isUpdated = false

            transaction {
                user?.let {
                    it.name = newData.name
                    it.email = newData.email
                    it.password = newData.password
                    it.photoUrl = newData.photoUrl
                    isUpdated = it.flush()
                }
            }

            call.respond(HttpStatusCode.OK, isUpdated)
        }

        post("/") {
            val user = call.receive<User>()

            val created = transaction {
                UserEntity.new {
                    name = user.name
                    email = user.email
                    password = user.password
                    photoUrl = user.photoUrl
                }
            }

            call.respond(
                status = HttpStatusCode.Created,
                message = created.id.value
            )
        }
    }
}