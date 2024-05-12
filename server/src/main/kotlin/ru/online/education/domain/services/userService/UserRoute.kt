package ru.online.education.domain.services.userService

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ru.online.education.core.util.respond
import ru.online.education.domain.repository.UserRepository
import ru.online.education.domain.repository.model.UserDto
import ru.online.education.domain.services.account.auth.jwtAuthenticate
import util.map

fun Route.userRoute() {
    route("/user") {
        jwtAuthenticate {
            get("/{id}") {
                val userService by application.inject<UserRepository>()
                val id = call.parameters["id"]?.toInt() ?: 0
                respond(userService.getById(id))
            }
            get("all/{page}") {
                val userRepository by application.inject<UserRepository>()
                val page = call.parameters["page"]?.toInt() ?: 0
                respond(userRepository.getAll(page))
            }

            put {
                val userService by application.inject<UserRepository>()

                val user = call.receive<UserDto>()
                respond(userService.update(user).map { it!! })
            }
        }
    }
}
