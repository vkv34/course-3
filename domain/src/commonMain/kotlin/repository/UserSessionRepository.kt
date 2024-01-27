package repository

import model.UserSession
import repository.defaults.Repository
import util.ApiResult

interface UserSessionRepository : Repository<UserSession, String> {
    suspend fun checkSession(id: String): Boolean
}