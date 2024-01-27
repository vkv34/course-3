package ru.online.education.data.model

data class JwtParameters(
    val secret: String,
    val issuer: String,
    val audience: String
)