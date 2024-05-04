package ru.online.education.domain.services.account.auth

import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ru.online.education.domain.repository.UserSessionRepository
import ru.online.education.data.model.JwtParameters

fun Application.installJWTAuth() {
    val jwtParameters by inject<JwtParameters>()
    val userSessionRepository by inject<UserSessionRepository>()

    install(Authentication) {
        jwt("auth-jwt") {

            this.verifier(jwtParameters.issuer, jwtParameters.audience, Algorithm.HMAC256(jwtParameters.secret))

            validate { credential ->
                val sessionId = credential.payload.getClaim("sessionId").asString()
                if (sessionId.isNullOrEmpty()) {
                    null
                } else if (userSessionRepository.checkSession(sessionId)) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }

            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
}

fun Route.jwtAuthenticate(authScope: Route.() -> Unit) = authenticate("auth-jwt") { authScope() }
