package com.example.db.routing

import com.example.db.INVALID_ID_FORMAT
import com.example.db.models.Question
import com.example.db.models.QuestionController
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.questionRouting(controller: QuestionController) {
    route("/question") {

        get("/") {
            call.respond(
                status = HttpStatusCode.OK,
                message = controller.all()
            )
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toInt()

            id?.let {
                val question = controller.find(id)

                if (question == null) call.respond(HttpStatusCode.NotFound)
                else call.respond(HttpStatusCode.Found, question)

            } ?: call.respond(HttpStatusCode.BadRequest, INVALID_ID_FORMAT)
        }

        post("/") {
            val post = call.receive<Question>()

            call.respond(
                status = HttpStatusCode.Created,
                message = controller.create(post)
            )
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toInt()

            id?.let {
                controller.delete(id)
                call.respond(HttpStatusCode.OK)

            } ?: call.respond(HttpStatusCode.BadRequest, INVALID_ID_FORMAT)
        }

        put("/{id}") {
            val id = call.parameters["id"]?.toInt()

            id?.let {
                val newData = call.receive<Question>()

                controller.update(id, newData)
                call.respond(HttpStatusCode.OK)

            } ?: call.respond(HttpStatusCode.BadRequest, INVALID_ID_FORMAT)
        }
    }
}