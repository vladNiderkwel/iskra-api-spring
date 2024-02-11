package com.example.db.routing

import com.example.db.INVALID_ID_FORMAT
import com.example.db.models.Staff
import com.example.db.models.StaffController
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.staffRouting(controller: StaffController) {
    route("/staff") {

        get("/all") {
            call.respond(
                status = HttpStatusCode.OK,
                message = controller.all()
            )
        }

        get("/") {
            var staff: Staff? = null

            if (!call.request.queryParameters["id"].isNullOrEmpty()) {

                val id = call.request.queryParameters["id"]?.toInt()
                id?.let {
                    staff = controller.find(id)
                } ?: call.respond(HttpStatusCode.BadRequest, INVALID_ID_FORMAT)

            } else if (!call.request.queryParameters["email"].isNullOrEmpty()) {

                val email = call.request.queryParameters["email"]
                staff = controller.find(email!!)
            }

            if (staff == null) call.respond(HttpStatusCode.NotFound)
            else call.respond(HttpStatusCode.Found, staff!!)
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toInt()

            id?.let {
                val staff = controller.find(id)

                if (staff == null) call.respond(HttpStatusCode.NotFound)
                else call.respond(HttpStatusCode.Found, staff)

            } ?: call.respond(HttpStatusCode.BadRequest, INVALID_ID_FORMAT)
        }

        post("/") {
            val staff = call.receive<Staff>()

            call.respond(
                status = HttpStatusCode.Created,
                message = controller.create(staff)
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
                val newData = call.receive<Staff>()

                controller.update(id, newData)
                call.respond(HttpStatusCode.OK)

            } ?: call.respond(HttpStatusCode.BadRequest, INVALID_ID_FORMAT)
        }
    }
}