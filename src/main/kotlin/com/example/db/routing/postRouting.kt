package com.example.db.routing

import com.example.db.models.Post
import com.example.db.models.PostEntity
import com.example.db.models.User
import com.example.db.models.UserEntity
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Routing.postRouting() {
    route("/post") {

        get {
            call.respondRedirect("/post/")
        }

        get("/") {
            call.respond(
                status = HttpStatusCode.OK,
                message = transaction {
                    PostEntity.all().map {
                        it.model()
                    }
                }
            )
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toInt()

            call.respondRedirect("/post/$id/")
        }

        get("/{id}/") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Неверный ID")

            val post = transaction {
                PostEntity.findById(id)?.model()
            }

            if(post == null) call.respond(HttpStatusCode.NotFound)
            else call.respond(HttpStatusCode.Found, post)
        }

        post("/") {
            val post = call.receive<Post>()

            val user = transaction {
                UserEntity.findById(post.author.id)
            }

            var created = false

            user?.let {
                transaction {
                    PostEntity.new {
                        title = post.title
                        body = post.body
                        author = user
                    }
                }
                created = true
            }

            if (created) call.respond(HttpStatusCode.Created)
            else
                call.respond(
                    status = HttpStatusCode.NotFound,
                    message = "Подобного пользователя не существует"
                )
        }

        delete("/{id}/") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Неверный ID")
            transaction {
                PostEntity.findById(id)?.delete()
            }
            call.respond(HttpStatusCode.OK)
        }

        put("/{id}/") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Неверный ID")

            val newData = call.receive<Post>()

            val post = transaction {
                PostEntity.findById(id)
            }

            var isUpdated = false

            transaction {
                post?.let {
                    it.title = newData.title
                    it.body = newData.body
                    isUpdated = it.flush()
                }
            }

            call.respond(HttpStatusCode.OK, isUpdated)
        }
    }
}