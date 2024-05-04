package ru.online.education.domain.services.userService

import ru.online.education.domain.repository.UserRepository

class UserService(private val userRepository: UserRepository) {
    suspend fun getDefaultUser() /*userRepository.getAll(1).first()*/ {
        TODO("Not yet implemented")
    }
}
