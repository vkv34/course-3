package ru.online.education.domain.services.userSession

import ru.online.education.domain.repository.model.ListResponse
import ru.online.education.domain.repository.model.SessionState
import ru.online.education.domain.repository.model.UserSession
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.update
import ru.online.education.domain.repository.UserSessionRepository
import ru.online.education.core.exception.SelectExeption
import ru.online.education.core.util.apiCall
import ru.online.education.core.util.dbCall
import ru.online.education.data.table.UserSessionTable
import ru.online.education.data.table.UsersTable
import ru.online.education.di.dbQuery
import util.ApiResult
import java.util.*

class UserSessionRepositoryImpl : UserSessionRepository {
    override suspend fun checkSession(id: String): Boolean = (getById(id) as? ApiResult.Success)?.data?.state == SessionState.Started

    override suspend fun getAll(page: Int): ApiResult<ListResponse<UserSession>> =
        dbCall(
            call = {
                ListResponse(
                    dbQuery {
                        UserSessionTable
                            .selectAll()
                            .limit(n = pageSize, offset = (page * pageSize).toLong())
                            .map(::resultRowToUserSession)
                    },
                )
            },
        )

    override suspend fun getById(id: String): ApiResult<UserSession> =
        dbCall(
            call = {
                dbQuery {
                    UserSessionTable
                        .selectAll()
                        .where { UserSessionTable.id eq UUID.fromString(id) }
                        .singleOrNull()
                        ?.toUserSession()
                } ?: throw SelectExeption("Сессия $id не найдена")
            },
        )

    override suspend fun deleteById(id: String): ApiResult<UserSession> =
        apiCall {
            dbQuery {
                UserSessionTable
                    .update({ UserSessionTable.id eq UUID.fromString(id) }) {
                        it[state] = SessionState.Ended
                    }
            }
            getById(id)
        }

    override suspend fun update(data: UserSession): ApiResult<UserSession?> {
        TODO("Not yet implemented")
    }

    override suspend fun add(data: UserSession): ApiResult<UserSession> =
        apiCall {
            val id =
                dbQuery {
                    UserSessionTable.insertAndGetId {
                        userToInsertStatement(it, data)
                    }.value.toString()
                }
            getById(id)
        }

    private fun resultRowToUserSession(row: ResultRow) = row.toUserSession()

    private fun ResultRow.toUserSession() =
        UserSession(
            id = this[UserSessionTable.id].value.toString(),
            userId = this[UserSessionTable.user].value,
            lastOnline = this[UserSessionTable.lastOnline],
            authDate = this[UserSessionTable.authDate],
            state = this[UserSessionTable.state],
            host = this[UserSessionTable.host],
        )

    private fun <T : Any> UserSessionTable.userToInsertStatement(
        statement: InsertStatement<T>,
        session: UserSession,
    ) {
        with(session) {
            val userId = EntityID(userId, UsersTable)
            statement[UserSessionTable.user] = userId
//            statement[UserSessionTable.lastOnline] = lastOnline
//            statement[UserSessionTable.authDate] = authDate
            statement[UserSessionTable.host] = host
            statement[UserSessionTable.state] = state
        }
    }
}
