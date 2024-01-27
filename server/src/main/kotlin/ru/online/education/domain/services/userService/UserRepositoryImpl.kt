package ru.online.education.domain.services.userService

import model.User
import model.UserRole
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.InsertStatement
import repository.UserRepository
import ru.online.education.core.exception.InsertErrorException
import ru.online.education.data.table.UserRoleTable
import ru.online.education.data.table.UsersTable
import ru.online.education.di.dbQuery

class UserRepositoryImpl : UserRepository {

    private fun resultRowToUser(row: ResultRow) = User(
        id = row[UsersTable.id].value,
        email = row[UsersTable.email],
        password = row[UsersTable.password],
        firstName = row[UsersTable.firstName],
        secondName = row[UsersTable.secondName],
        lastName = row[UsersTable.lastName],
        role = UserRole.entries[row[UsersTable.userRole].value.dec()]
    )

    private fun ResultRow.toUser() = User(
        id = this[UsersTable.id].value,
        email = this[UsersTable.email],
        password = this[UsersTable.password],
        firstName = this[UsersTable.firstName],
        secondName = this[UsersTable.secondName],
        lastName = this[UsersTable.lastName],
        role = UserRole.entries[this[UsersTable.userRole].value.dec()]
    )

    private fun <T : Any> UsersTable.userToInsertStatement(statement: InsertStatement<T>, user: User) {
        val roleId = EntityID(user.role.ordinal.inc(), UserRoleTable)
        statement[id] = user.id
        statement[email] = user.email
        statement[password] = user.password
        statement[firstName] = user.firstName
        statement[secondName] = user.secondName
        statement[lastName] = user.lastName
        statement[userRole] = roleId
    }

    override suspend fun getAllUsers(page: Int): List<User> = dbQuery {
        UsersTable
            .selectAll()
            .limit(n = pageSize, offset = (page * pageSize).toLong())
            .map(::resultRowToUser)
    }

    override suspend fun getUserById(id: Int): User? =
        dbQuery {
            UsersTable
                .selectAll()
                .where { UsersTable.id eq id }
                .map(::resultRowToUser)
                .firstOrNull()
        }

    override suspend fun findUserByEmail(user: User): User? = dbQuery {
        UsersTable
            .selectAll()
            .where {
                UsersTable.email eq user.email
            }
            .singleOrNull()
            ?.toUser()
    }

    override suspend fun addUser(user: User): User {
        val id = dbQuery {
            UsersTable.insertAndGetId {
                userToInsertStatement(it, user)
            }
        }.value

        return getUserById(id) ?: throw InsertErrorException("addUser error")
    }

}