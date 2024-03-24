package repository

import model.AuthResponse
import model.UserDto
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