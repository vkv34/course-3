package ru.online.education.app.feature.account.domain.repository.impl

import domain.NotificationManager
import io.github.aakira.napier.Napier
import io.ktor.client.*
import ru.online.education.app.core.util.ktorUtil.safeGet
import ru.online.education.app.core.util.ktorUtil.safePostAsJson
import ru.online.education.app.feature.account.domain.repository.AuthCallback
import ru.online.education.domain.repository.AccountRepository
import ru.online.education.domain.repository.model.AuthRequest
import ru.online.education.domain.repository.model.AuthResponse
import ru.online.education.domain.repository.model.UserDto
import util.ApiResult

class AccountRepositoryImpl(
    private val client: HttpClient,
    private val authCallback: AuthCallback,
    val notificationManager: NotificationManager
) : AccountRepository {
    override suspend fun loginByEmailAndPassword(
        login: CharSequence,
        password: CharSequence,
        hostName: String
    ): ApiResult<AuthResponse> {
        val response: ApiResult<AuthResponse> = client.safePostAsJson(
            path = "account/signIn",
            body = AuthRequest(
                email = login.toString(),
                password = password.toString(),

                ),
            notificationManager = notificationManager
        )

        if (response is ApiResult.Success) {
            authCallback.onAuth(response.data)
        }

        Napier.d { response.message }

        return response
    }

    override suspend fun createAccount(userDto: UserDto): ApiResult<UserDto> = client.safePostAsJson(
        path = "account/signUp",
        body = userDto,
        notificationManager = notificationManager
    )

    override suspend fun logOut() {
        authCallback.onLogOut()
    }

    override suspend fun getTestAdminAccount(): ApiResult<AuthResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun currentUser(): ApiResult<UserDto> =
        client.safeGet<UserDto>("/account/current", notificationManager)
}