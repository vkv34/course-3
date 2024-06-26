package ru.online.education.domain.services.userService

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.update
import ru.online.education.core.exception.SelectExeption
import ru.online.education.core.util.apiCall
import ru.online.education.core.util.dbCall
import ru.online.education.data.table.UserRoleTable
import ru.online.education.data.table.UsersTable
import ru.online.education.di.dbQuery
import ru.online.education.domain.repository.UserRepository
import ru.online.education.domain.repository.model.ListResponse
import ru.online.education.domain.repository.model.UserDto
import ru.online.education.domain.repository.model.UserRole
import util.ApiResult
import util.map

class UserRepositoryImpl : UserRepository {
    private fun resultRowToUser(row: ResultRow) =
        UserDto(
            id = row[UsersTable.id].value,
            email = row[UsersTable.email],
            password = row[UsersTable.password],
            firstName = row[UsersTable.firstName],
            secondName = row[UsersTable.secondName],
            lastName = row[UsersTable.lastName],
            role = UserRole.entries[row[UsersTable.userRole].value.dec()],
        )

    private fun ResultRow.toUser() =
        UserDto(
            id = this[UsersTable.id].value,
            email = this[UsersTable.email],
            password = this[UsersTable.password],
            firstName = this[UsersTable.firstName],
            secondName = this[UsersTable.secondName],
            lastName = this[UsersTable.lastName],
            role = UserRole.entries[this[UsersTable.userRole].value.dec()],
        )

    private fun <T : Any> UsersTable.userToInsertStatement(
        statement: InsertStatement<T>,
        user: UserDto,
    ) {
        val roleId = EntityID(user.role.ordinal.inc(), UserRoleTable)
        statement[id] = user.id
        statement[email] = user.email
        statement[password] = user.password
        statement[firstName] = user.firstName
        statement[secondName] = user.secondName
        statement[lastName] = user.lastName
        statement[userRole] = roleId
    }

    /* override suspend fun getAllUsers(page: Int): List<UserDto> = dbQuery {
         UsersTable
             .selectAll()
             .limit(n = pageSize, offset = (page * pageSize).toLong())
             .map(::resultRowToUser)
     }

     override suspend fun getUserById(id: Int): UserDto? =
     */

    override suspend fun findUserByEmail(user: UserDto): UserDto? =
        dbQuery {
            UsersTable
                .selectAll()
                .where {
                    UsersTable.email eq user.email
                }
                .singleOrNull()
                ?.toUser()
        }
    /*
        override suspend fun addUser(user: UserDto): UserDto {
            val id = dbQuery {
                UsersTable.insertAndGetId {
                    userToInsertStatement(it, user)
                }
            }.value

            return getUserById(id) ?: throw InsertErrorException("addUser error")
        }*/

    override suspend fun getAll(page: Int): ApiResult<ListResponse<UserDto>> = dbCall {
        ListResponse(
            dbQuery {
                UsersTable
                    .selectAll()
                    .limit(n = pageSize, offset = pageSize * page.toLong())
                    .map(::resultRowToUser)
            }
        )
    }

    override suspend fun getById(id: Int): ApiResult<UserDto> =
        dbCall(
            call = {
                dbQuery {
                    UsersTable
                        .selectAll()
                        .where { UsersTable.id eq id }
                        .map(::resultRowToUser)
                        .firstOrNull()
                } ?: throw SelectExeption("Пользователь не найден")
            },
        )

    override suspend fun deleteById(id: Int): ApiResult<UserDto> {
        TODO("Not yet implemented")
    }

    override suspend fun update(data: UserDto): ApiResult<UserDto?> = apiCall {
        dbQuery {
            UsersTable.update({ UsersTable.id eq data.id }) {
                it[email] = data.email
                it[firstName] = data.firstName
                it[secondName] = data.secondName
                it[lastName] = data.lastName
                it[password] = data.password
            }
        }

        getById(data.id)
    }.map { it }

    override suspend fun add(data: UserDto): ApiResult<UserDto> = apiCall {
        val id = dbQuery {
            UsersTable
                .insertAndGetId {
                    it[email] = data.email
                    it[firstName] = data.firstName
                    it[secondName] = data.secondName
                    it[lastName] = data.lastName
                    it[password] = data.password
                    it[userRole] = EntityID(UserRole.entries.indexOf(data.role) + 1, UserRoleTable)
                }.value
        }
        getById(id)
    }
}
