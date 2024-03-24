package repository

import model.UserDto
import repository.defaults.DefaultPaging
import repository.defaults.Repository

interface UserRepository : Repository<UserDto, Int> {
//    suspend fun getAllUsers(page: Int): List<UserDto>
//
//    suspend fun getUserById(id: Int): UserDto?
//
    suspend fun findUserByEmail(user: UserDto): UserDto?
//
//    suspend fun addUser(user: UserDto) : UserDto
}