package com.example.db.routing

import com.example.db.INVALID_ID_FORMAT
import com.example.db.models.Achievement
import com.example.db.models.AchievementController
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.achievementRouting(controller: AchievementController) {
    route("/achievements") {

        get("/") {
            call.respond(
                status = HttpStatusCode.OK,
                message = controller.all()
            )
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toInt()

            id?.let {
                val achievement = controller.find(it)

                if (achievement == null) call.respond(HttpStatusCode.NotFound)
                else call.respond(HttpStatusCode.Found, achievement)

            } ?: call.respond(HttpStatusCode.BadRequest, INVALID_ID_FORMAT)
        }

        post("/") {
            val achievement = call.receive<Achievement>()

            call.respond(
                status = HttpStatusCode.Created,
                message = controller.create(achievement)
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
                val newData = call.receive<Achievement>()

                controller.update(id, newData)
                call.respond(HttpStatusCode.OK)

            } ?: call.respond(HttpStatusCode.BadRequest, INVALID_ID_FORMAT)
        }
    }
}