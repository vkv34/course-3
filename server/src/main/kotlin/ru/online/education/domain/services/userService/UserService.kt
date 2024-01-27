package ru.online.education.domain.services.userService

import repository.UserRepository

class UserService(private val userRepository: UserRepository) {
    suspend fun getDefaultUser() = userRepository.getAllUsers(1).first()
}