package org.inc

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        route("/amazingApi") {
            post("/postMyAmazingString") { 
                val str: String = call.receiveText()
                call.respondText(str)
            }
        }
    }
}
