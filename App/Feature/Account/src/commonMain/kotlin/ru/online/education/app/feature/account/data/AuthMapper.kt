package ru.online.education.app.feature.account.data

import ru.online.education.domain.repository.model.AuthRequest
import ru.online.education.app.feature.account.domain.model.Auth

fun Auth.toAuthRequest() = AuthRequest(
    email = email,
    password = password
)