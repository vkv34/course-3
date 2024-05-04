package ru.online.education.app.feature.di

import ru.online.education.app.feature.di.httpClient.authPlugin
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import ru.online.education.app.core.util.api.baseUrl
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
                        serializersModule = api.defaultSerializersModule
                        prettyPrint = true
                    }
                )
            }
            install(Logging) {
                level = LogLevel.ALL
            }

            install(authPlugin(authstore))

            defaultRequest {
                url(baseUrl)
            }


        }
    }
}