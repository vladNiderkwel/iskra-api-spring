package com.example.db.routing

import com.example.db.INVALID_ID_FORMAT
import com.example.db.models.CompoundTask
import com.example.db.models.QuestionTaskController
import com.example.db.models.Task
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.questionTaskRouting(controller: QuestionTaskController) {
    route("/compound-task") {

        get("/") {
            call.respond(
                status = HttpStatusCode.OK,
                message = controller.all()
            )
        }

        post("/") {
            val task = call.receive<Task>()

            call.respond(
                status = HttpStatusCode.Created,
                message = controller.create(task)
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
                val newData = call.receive<CompoundTask>()

                controller.update(id, newData)
                call.respond(HttpStatusCode.OK)

            } ?: call.respond(HttpStatusCode.BadRequest, INVALID_ID_FORMAT)
        }
    }
}