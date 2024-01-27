package ru.online.education.di

import org.jetbrains.exposed.crypt.Algorithms

val passwordEnryptor = Algorithms.BLOW_FISH("00112233445566778899AABBCCDDEEFF0000111122223333")
val entityEncryptor = Algorithms.BLOW_FISH("00112233445566778899AABBCCDDEEFF0000111122223333")
