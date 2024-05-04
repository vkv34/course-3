package ru.online.education.core.util

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.application.ApplicationCallPipeline.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.online.education.domain.repository.model.BaseModel
import ru.online.education.domain.repository.model.SessionState
import ru.online.education.domain.repository.model.UserRole
import org.koin.ktor.ext.inject
import ru.online.education.domain.repository.UserRepository
import ru.online.education.domain.repository.UserSessionRepository
import util.ApiResult

inline fun <reified T : BaseModel> Route.role(roles: Iterable<UserRole>) {
    intercept(ApplicationPhase.Call) {
        val userRepository by application.inject<UserRepository>()
        val userSessionRepository by application.inject<UserSessionRepository>()

        val principal = call.principal<JWTPrincipal>()
        if (principal == null) {
            call.respond(HttpStatusCode.Unauthorized, ApiResult.Error<T>("Не верный токен") as ApiResult<T>)
            finish()
        }

        val sessionId = principal?.payload?.claims?.get("sessionId")?.asString()
        if (sessionId.isNullOrEmpty()) {
            call.respond(HttpStatusCode.Unauthorized, ApiResult.Error<T>("Не верный токен") as ApiResult<T>)
            finish()
        }
        checkNotNull(sessionId)
        val userSession = userSessionRepository.getById(sessionId)
        if (userSession !is ApiResult.Success) {
            call.respond(HttpStatusCode.Unauthorized, ApiResult.Error<T>("Не верный токен") as ApiResult<T>)
            finish()
            return@intercept
        } else if (userSession.data.state == SessionState.Ended) {
            call.respond(HttpStatusCode.Unauthorized, ApiResult.Error<T>("Сессия завершена") as ApiResult<T>)
            finish()
        }
        val userId = userSession.data.userId
        val user = userRepository.getById(userId)

        if (user is ApiResult.Success) {
            if (!roles.any{it == user.data.role}) {
                call.respond(HttpStatusCode.Forbidden, ApiResult.Error<T>("Не достаточно прав, необходимые роли: ${roles.joinToString(", ")}") as ApiResult<T>)
                finish()
            }
        } else {
            call.respond(HttpStatusCode.Unauthorized, ApiResult.Error<T>("Пользователь не найден"))
            finish()
        }
    }
}

inline fun <reified T : BaseModel> Route.role(role: UserRole) = role<T>(listOf(role))
