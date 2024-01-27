package repository

import model.User
import repository.defaults.DefaultPaging

interface UserRepository : DefaultPaging {
    suspend fun getAllUsers(page: Int): List<User>
    
    suspend fun getUserById(id: Int): User?

    suspend fun findUserByEmail(user: User): User?
    
    suspend fun addUser(user: User) : User
}