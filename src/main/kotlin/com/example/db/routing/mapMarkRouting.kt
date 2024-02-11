package com.example.db.routing

import com.example.db.INVALID_ID_FORMAT
import com.example.db.models.MapMark
import com.example.db.models.MapMarksController
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.mapMarkRouting(controller: MapMarksController) {
    route("/marks") {

        get("/") {
            call.respond(
                status = HttpStatusCode.OK,
                message = controller.all()
            )
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toInt()

            id?.let {
                val mark = controller.find(it)

                if (mark == null) call.respond(HttpStatusCode.NotFound)
                else call.respond(HttpStatusCode.Found, mark)

            } ?: call.respond(HttpStatusCode.BadRequest, INVALID_ID_FORMAT)
        }

        post("/") {
            val mark = call.receive<MapMark>()

            call.respond(
                status = HttpStatusCode.Created,
                message = controller.create(mark)
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
                val newData = call.receive<MapMark>()

                controller.update(id, newData)
                call.respond(HttpStatusCode.OK)

            } ?: call.respond(HttpStatusCode.BadRequest, INVALID_ID_FORMAT)
        }
    }
}