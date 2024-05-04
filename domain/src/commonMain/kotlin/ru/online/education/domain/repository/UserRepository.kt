package ru.online.education.domain.repository

import ru.online.education.domain.repository.model.UserDto
import ru.online.education.domain.repository.defaults.DefaultPaging
import ru.online.education.domain.repository.defaults.Repository

interface UserRepository : Repository<UserDto, Int> {
//    suspend fun getAllUsers(page: Int): List<UserDto>
//
//    suspend fun getUserById(id: Int): UserDto?
//
    suspend fun findUserByEmail(user: UserDto): UserDto?

//
//    suspend fun addUser(user: UserDto) : UserDto
}