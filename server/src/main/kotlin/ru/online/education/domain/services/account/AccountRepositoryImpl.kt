package ru.online.education.domain.services.account

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import ru.online.education.domain.repository.model.AuthResponse
import ru.online.education.domain.repository.model.SessionState
import ru.online.education.domain.repository.model.UserDto
import ru.online.education.domain.repository.model.UserSession
import ru.online.education.domain.repository.AccountRepository
import ru.online.education.domain.repository.UserRepository
import ru.online.education.domain.repository.UserSessionRepository
import util.ApiResult
import java.util.*
import java.util.concurrent.TimeUnit

class AccountRepositoryImpl(
    val userRepository: UserRepository,
    val userSessionRepository: UserSessionRepository,
    val secret: String,
    val issuer: String,
    val audience: String,
) : AccountRepository {
    override suspend fun loginByEmailAndPassword(
        login: CharSequence,
        password: CharSequence,
        hostName: String,
    ): ApiResult<AuthResponse>  {
        val user = userRepository.findUserByEmail(UserDto(email = login.toString(), password = password.toString()))
        return if (user?.password == password) {
            authWithSession(user, hostName)
        } else {
            ApiResult.Error("Пользователь с такими данными не найден")
        }
    }

    override suspend fun logOut() {
        TODO("Not yet implemented")
    }

    override suspend fun getTestAdminAccount(): ApiResult<AuthResponse>/* =
        authByUser(userRepository.getUserById(1))*/  {
        TODO()
    }

    override suspend fun currentUser(): ApiResult<UserDto> {
        TODO("Not yet implemented")
    }

    private suspend fun authWithSession(
        user: UserDto?,
        hostName: String,
    ) = try {
        if (user == null) {
            ApiResult.Error(message = "Пользователь не найден")
        } else
            {
                val userSession =
                    UserSession(
                        userId = user.id,
                        state = SessionState.Started,
                        host = hostName,
                    )
                val id = userSessionRepository.add(userSession)
                if (id !is ApiResult.Success) {
                    throw Exception("Error")
                }
                val token =
                    JWT.create()
                        .withAudience(audience)
                        .withIssuer(issuer)
                        .withExpiresAt(Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(10)))
                        .withClaim("sessionId", id.data.id)
                        .sign(Algorithm.HMAC256(secret))
                ApiResult.Success(
                    AuthResponse(
                        token = token,
                    ),
                    message = "Успешная авторизация",
                )
            }
    } catch (e: Exception) {
        ApiResult.Error(message = e.localizedMessage ?: "Ошибка сервера", e)
    }

    private fun authByUser(user: UserDto?) =
        try {
            if (user == null) {
                ApiResult.Error(message = "Пользователь не найден")
            } else {
                val token =
                    JWT.create()
                        .withAudience(audience)
                        .withIssuer(issuer)
                        .withExpiresAt(Date(System.currentTimeMillis() + 60000))
                        .withClaim("userId", user.id)
                        .sign(Algorithm.HMAC256(secret))
                ApiResult.Success(
                    AuthResponse(
                        token = token,
                    ),
                    message = "Успешная авторизация",
                )
            }
        } catch (e: Exception) {
            ApiResult.Error(message = e.localizedMessage ?: "Ошибка сервера", e)
        }
}
