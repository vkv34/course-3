package ru.online.education.di

import io.ktor.server.application.*
import org.koin.dsl.module
import ru.online.education.data.model.JwtParameters

fun getJwtModule(application: Application) = module {
    with(application){
        val jwtParameters = JwtParameters(
            secret = environment.config.property("jwt.secret").getString(),
            issuer = environment.config.property("jwt.issuer").getString(),
            audience = environment.config.property("jwt.audience").getString()
        )

        single { jwtParameters }
    }
}
