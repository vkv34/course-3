package ru.online.education.app.feature.account.domain.model

data class Auth(
    val email:  String = "email",
    val password: String = "password",
    val error: String = ""
)
