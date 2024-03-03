package ru.online.education.domain.services.account.currentUser

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*
import model.BaseModel
import model.User
import model.UserSession
import org.koin.ktor.ext.inject
import repository.UserSessionRepository
import util.ApiResult
import ru.online.education.core.util.respond
import ru.online.education.core.exception.AuthException

suspend fun getCurrentUser(
    userSessionRepository: UserSessionRepository,
    sessionId: String
) = userSessionRepository.getById(sessionId)

suspend fun PipelineContext<Unit, ApplicationCall>.getCurrentSession(): UserSession {
    val repo by application.inject<UserSessionRepository>()
    val sessionId = call.principal<JWTPrincipal>()?.get("sessionId") ?: ""
    val result = repo.getById(sessionId)
    if (result is ApiResult.Success) {
        return result.data
    } else {
        respond(ApiResult.Error<UserSession>("ОШибка при авторизации", AuthException()))
        finish()
    }
    throw AuthException()
}
