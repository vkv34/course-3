package ru.online.education.domain.services.account.auth

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.online.education.domain.repository.model.BaseModel
import ru.online.education.domain.repository.model.UserDto
import ru.online.education.domain.repository.model.UserRole
import org.koin.ktor.ext.inject
import ru.online.education.domain.repository.AccountRepository
import ru.online.education.domain.repository.UserRepository
import ru.online.education.domain.repository.UserSessionRepository
import ru.online.education.core.util.respond
import ru.online.education.core.util.role
import ru.online.education.domain.services.account.AccountService
import util.ApiResult
import java.util.logging.Logger

fun Routing.installAccountRoute() {
    val accountService by application.inject<AccountService>()

    route("/account") {
        post("signIn") {
            val user = call.receive<UserDto>()
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

                Logger.getLogger("AccountLogger").info(userId.message)

                if (userId is ApiResult.Success) {
                    val user = userRepository.getById(userId.data.userId)
                    respond(user)
                } else {
                    respond(ApiResult.Error<UserDto>("Вы не авторизованы"))
                }
            }
        }

        get("test") {
            val accountRepository by application.inject<AccountRepository>()
        }
    }
}
