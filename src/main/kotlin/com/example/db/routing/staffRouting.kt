package com.example.db.routing

import com.example.db.INVALID_ID
import com.example.db.models.Staff
import com.example.db.models.StaffController
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.staffRouting(staffController: StaffController) {
    route("/staff") {

        get("/all") {
            call.respond(
                status = HttpStatusCode.OK,
                message = staffController.all()
            )
        }

        get("/") {
            var staff: Staff? = null

            if (!call.request.queryParameters["id"].isNullOrEmpty()) {

                val id = call.request.queryParameters["id"]?.toInt() ?: throw IllegalArgumentException("Неверный ID")
                staff = staffController.find(id)
            } else if (!call.request.queryParameters["email"].isNullOrEmpty()) {

                val email = call.request.queryParameters["email"] ?: throw IllegalArgumentException("Неверный ID")
                staff = staffController.find(email)
            }

            if (staff == null) call.respond(HttpStatusCode.NotFound)
            else call.respond(HttpStatusCode.Found, staff)
        }

        get("/{id}/") {
            val id = call.parameters["id"]?.toInt()

            id?.let {
                val staff = staffController.find(id)

                if (staff == null) call.respond(HttpStatusCode.NotFound)
                else call.respond(HttpStatusCode.Found, staff)

            } ?: call.respond(HttpStatusCode.BadRequest, INVALID_ID)
        }

        post("/") {
            val staff = call.receive<Staff>()

            call.respond(
                status = HttpStatusCode.Created,
                message = staffController.create(staff)
            )
        }

        delete("/{id}/") {
            val id = call.parameters["id"]?.toInt()

            id?.let {
                staffController.delete(id)
                call.respond(HttpStatusCode.OK)

            } ?: call.respond(HttpStatusCode.BadRequest, INVALID_ID)
        }

        put("/{id}") {
            val id = call.parameters["id"]?.toInt()

            id?.let {
                val newData = call.receive<Staff>()

                staffController.update(id, newData)
                call.respond(HttpStatusCode.OK)

            } ?: call.respond(HttpStatusCode.BadRequest, INVALID_ID)
        }
    }
}