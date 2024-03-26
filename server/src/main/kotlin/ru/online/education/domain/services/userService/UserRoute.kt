package ru.online.education.domain.services.userService

import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import repository.UserRepository
import ru.online.education.core.util.respond
import ru.online.education.domain.services.account.auth.jwtAuthenticate

fun Route.userRoute() {
    route("/user") {
        jwtAuthenticate {
            get("/{id}") {
                val userService by application.inject<UserRepository>()
                val id = call.parameters["id"]?.toInt() ?: 0
                respond(userService.getById(id))
            }
        }
    }
}