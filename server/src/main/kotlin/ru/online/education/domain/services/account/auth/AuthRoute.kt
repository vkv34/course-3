package ru.online.education.domain.services.account.auth

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.BaseModel
import model.User
import model.UserRole
import org.koin.ktor.ext.inject
import repository.AccountRepository
import repository.UserRepository
import repository.UserSessionRepository
import ru.online.education.core.util.role
import ru.online.education.domain.services.account.AccountService


fun Routing.installAccountRoute() {

    val accountService by application.inject<AccountService>()

    route("/account") {
        post("signIn") {
            val user = call.receive<User>()
            val host = call.request.headers[HttpHeaders.UserAgent] ?: ""
            println(host)
            call.respond(HttpStatusCode.OK, accountService.loginByEmailAndPassword(user.email, user.password, host))
        }
        post("signUp") {

        }

        jwtAuthenticate {
            role<BaseModel>(UserRole.all)

            get("current") {

                val userRepository by application.inject<UserRepository>()
                val userSessionRepository by application.inject<UserSessionRepository>()
                val sessionId = call.principal<JWTPrincipal>()?.get("sessionId") ?: "asd"
                val userId = userSessionRepository.getById(sessionId)
                val user = userRepository.getUserById(userId?.userId ?: 0)
                call.respondText(user?.email ?: "no user found")
            }
        }

        get("test") {
            val accountRepository by application.inject<AccountRepository>()
        }
    }
} 
