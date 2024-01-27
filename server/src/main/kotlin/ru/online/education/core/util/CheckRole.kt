package ru.online.education.core.util

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.application.ApplicationCallPipeline.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.BaseModel
import model.SessionState
import model.UserRole
import org.koin.ktor.ext.inject
import repository.UserRepository
import repository.UserSessionRepository
import util.ApiResult

inline fun <reified T : BaseModel> Route.role(roles: Iterable<UserRole>) {

    intercept(ApplicationPhase.Call) {
        val userRepository by application.inject<UserRepository>()
        val userSessionRepository by application.inject<UserSessionRepository>()

        val principal = call.principal<JWTPrincipal>()
        if (principal == null) {
            call.respond(HttpStatusCode.Unauthorized, ApiResult.Error<T>("Не верный токен"))
            finish()
        }

        val sessionId = principal?.payload?.claims?.get("sessionId")?.asString()
        if (sessionId.isNullOrEmpty()) {
            call.respond(HttpStatusCode.Unauthorized, ApiResult.Error<T>("Не верный токен"))
            finish()
        }
        checkNotNull(sessionId)
        val userSession = userSessionRepository.getById(sessionId)
        if (userSession == null) {
            call.respond(HttpStatusCode.Unauthorized, ApiResult.Error<T>("Не верный токен"))
            finish()
        } else if (userSession.state == SessionState.Ended) {
            call.respond(HttpStatusCode.Unauthorized, ApiResult.Error<T>("Сессия завершена"))
            finish()
        }
        checkNotNull(userSession)
        val userId = userSession.userId
        val user = userRepository.getUserById(userId)

        if (user == null) {
            call.respond(HttpStatusCode.Unauthorized, ApiResult.Error<T>("Пользователь не найден"))
            finish()
        }

        if (user != null) {
            if (!roles.any { user.role == it }) {
                call.respond(HttpStatusCode.Forbidden, ApiResult.Error<T>("Не достаточно прав"))
                finish()

            }


        }
    }


}

inline fun <reified T : BaseModel> Route.role(role: UserRole) = role<T>(listOf(role))
