package ru.online.education.domain.repository

import ru.online.education.domain.repository.model.UserSession
import ru.online.education.domain.repository.defaults.Repository
import util.ApiResult

interface UserSessionRepository : Repository<UserSession, String> {
    suspend fun checkSession(id: String): Boolean
}