package ru.online.education.domain.repository

import ru.online.education.domain.repository.model.AuthResponse
import ru.online.education.domain.repository.model.UserDto
import util.ApiResult

interface AccountRepository {
    /**
     *
     */
    suspend fun loginByEmailAndPassword(login: CharSequence, password: CharSequence, hostName: String): ApiResult<AuthResponse>

    suspend fun logOut()

    suspend fun getTestAdminAccount(): ApiResult<AuthResponse>

    suspend fun currentUser(): ApiResult<UserDto>
}