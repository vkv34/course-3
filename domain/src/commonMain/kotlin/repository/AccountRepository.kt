package repository

import model.LoginResponse
import util.ApiResult

interface AccountRepository {
    /**
     *
     */
    suspend fun loginByEmailAndPassword(login: CharSequence, password: CharSequence, hostName: String): ApiResult<LoginResponse>

    suspend fun getTestAdminAccount(): ApiResult<LoginResponse>
}