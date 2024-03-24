package ru.online.education.app.feature.di

import api.serializersModule
import httpClient.authPlugin
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import model.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.online.education.app.feature.account.domain.repository.UserAuthStore

val ktorClientModule = module {
    single {
        val authstore = get<UserAuthStore>()

        HttpClient {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                        serializersModule = api.serializersModule
                    }
                )
            }
            install(Logging) {
                level = LogLevel.ALL
            }

            install(authPlugin(authstore))

            defaultRequest {
                url("http://45.146.164.243:8080/")
            }


        }
    }
}