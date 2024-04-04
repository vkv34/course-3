package ru.online.education.domain.services.account.currentUser

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.util.pipeline.*
import model.UserDto
import model.UserSession
import org.koin.ktor.ext.inject
import repository.UserRepository
import repository.UserSessionRepository
import ru.online.education.core.exception.AuthException
import ru.online.education.core.util.respond
import util.ApiResult

// suspend fun getCurrentUser(
//    userSessionRepository: UserSessionRepository,
//    sessionId: String
// ) = userSessionRepository.getById(sessionId)

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

suspend fun PipelineContext<Unit, ApplicationCall>.getCurrentUser(): UserDto? {
    val session = getCurrentSession()
    val userRepository by application.inject<UserRepository>()
    val user = userRepository.getById(session.userId)
    return user.successOrNull()
}
