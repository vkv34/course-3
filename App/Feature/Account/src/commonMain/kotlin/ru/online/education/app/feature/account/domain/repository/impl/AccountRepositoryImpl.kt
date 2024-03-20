package ru.online.education.app.feature.account.domain.repository.impl

import io.ktor.client.*
import io.ktor.client.call.*
import model.AuthRequest
import model.AuthResponse
import repository.AccountRepository
import ru.online.education.app.core.util.ktorUtil.postAsJson
import ru.online.education.app.core.util.ktorUtil.safePostAsJson
import ru.online.education.app.feature.account.domain.repository.AuthCallback
import util.ApiResult

class AccountRepositoryImpl(
    private val client: HttpClient,
    private val authCallback: AuthCallback
) : AccountRepository {
    override suspend fun loginByEmailAndPassword(
        login: CharSequence,
        password: CharSequence,
        hostName: String
    ): ApiResult<AuthResponse> {
        val response: ApiResult<AuthResponse> = client.safePostAsJson(
            "account/signIn",
            AuthRequest(
                email = login.toString(),
                password = password.toString(),

                )
        )

        if (response is ApiResult.Success) {
            authCallback.onAuth(response.data)
        }

        return response
    }

    override suspend fun logOut() {
        TODO("Not yet implemented")
    }

    override suspend fun getTestAdminAccount(): ApiResult<AuthResponse> {
        TODO("Not yet implemented")
    }
}