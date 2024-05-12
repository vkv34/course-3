package ru.online.education

sealed class Platform {
    object Android : Platform()
    object Ios : Platform()
    object Browser : Platform()
    object Jvm : Platform()
}

expect fun currentPlatform(): Platform

