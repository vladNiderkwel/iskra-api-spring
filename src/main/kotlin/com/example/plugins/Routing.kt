package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureResources() {
    routing {
        // Static plugin. Try to access `/static/index.html`
//        staticFiles("/res") {
//            resources("static")
//        }
    }
}
