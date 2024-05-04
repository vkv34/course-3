package ru.online.education.app.feature.di.httpClient

import io.ktor.client.plugins.api.*
import io.ktor.http.*
import ru.online.education.app.feature.account.domain.repository.UserAuthStore


fun authPlugin(authStore: UserAuthStore) = createClientPlugin("AuthInterceptorPlugin") {
    this.onRequest { request, _ ->
        val token = authStore.read()?.token ?: ""
        request.headers.append(HttpHeaders.Authorization, "Bearer $token")
    }
}