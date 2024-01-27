package ru.online.education.domain.services.account

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import model.LoginResponse
import model.SessionState
import model.User
import model.UserSession
import repository.AccountRepository
import repository.UserRepository
import repository.UserSessionRepository
import util.ApiResult
import java.util.*
import java.util.concurrent.TimeUnit

class AccountRepositoryImpl(
    val userRepository: UserRepository,
    val userSessionRepository: UserSessionRepository,
    val secret: String,
    val issuer: String,
    val audience: String
) : AccountRepository {
    override suspend fun loginByEmailAndPassword(
        login: CharSequence,
        password: CharSequence,
        hostName: String
    ): ApiResult<LoginResponse>{
        val user = userRepository.findUserByEmail(User(email = login.toString(), password = password.toString()))
        return if (user?.password == password)
            authWithSession(user, hostName)
        else
            ApiResult.Error("Пользователь с такими данными не найден")
    }

    override suspend fun getTestAdminAccount(): ApiResult<LoginResponse> =
        authByUser(userRepository.getUserById(1))

    private suspend fun authWithSession(user: User?, hostName: String) = try{
        if (user == null) ApiResult.Error(message = "Пользователь не найден")
        else{
            val userSession = UserSession(
                userId = user.id,
                state = SessionState.Started,
                host = hostName
            )
            val id = userSessionRepository.add(userSession)
            val token = JWT.create()
                .withAudience(audience)
                .withIssuer(issuer)
                .withExpiresAt(Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(10)))
                .withClaim("sessionId", id)
                .sign(Algorithm.HMAC256(secret))
            ApiResult.Success(
                LoginResponse(
                    token = token
                ),
                message = "Успешная авторизация"
            )
        }
    } catch (e: Exception) {
        ApiResult.Error(message = e.localizedMessage ?: "Ошибка сервера", e)
    }


    private fun authByUser(user: User?) = try {
        if (user == null) ApiResult.Error(message = "Пользователь не найден")
        else {
            val token = JWT.create()
                .withAudience(audience)
                .withIssuer(issuer)
                .withExpiresAt(Date(System.currentTimeMillis() + 60000))
                .withClaim("userId", user.id)
                .sign(Algorithm.HMAC256(secret))
            ApiResult.Success(
                LoginResponse(
                    token = token
                ),
                message = "Успешная авторизация"
            )
        }
    } catch (e: Exception) {
        ApiResult.Error(message = e.localizedMessage ?: "Ошибка сервера", e)
    }
}