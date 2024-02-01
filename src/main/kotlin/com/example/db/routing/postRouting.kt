package com.example.db.routing

import com.example.db.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Routing.postRouting(postController: PostController) {
    route("/post") {

        get {
            call.respondRedirect("/post/")
        }

        get("/") {
            call.respond(
                status = HttpStatusCode.OK,
                message = postController.all()
            )
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toInt()

            call.respondRedirect("/post/$id/")
        }

        get("/{id}/") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Неверный ID")

            val post = postController.find(id)

            if (post == null) call.respond(HttpStatusCode.NotFound)
            else call.respond(HttpStatusCode.Found, post)
        }

        post("/") {
            val post = call.receive<Post>()

            call.respond(
                status = HttpStatusCode.Created,
                message = postController.create(post)
            )

            /*
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
                */
        }

        delete("/{id}/") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Неверный ID")
            postController.delete(id)
            call.respond(HttpStatusCode.OK)
        }

        put("/{id}/") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Неверный ID")

            val newData = call.receive<Post>()

            postController.update(id, newData)
            call.respond(HttpStatusCode.OK)
//            val post = transaction {
//                PostEntity.findById(id)
//            }
//
//            var isUpdated = false
//
//            transaction {
//                post?.let {
//                    it.title = newData.title
//                    it.body = newData.body
//                    isUpdated = it.flush()
//                }
//            }
//
//            call.respond(HttpStatusCode.OK, isUpdated)
        }
    }
}