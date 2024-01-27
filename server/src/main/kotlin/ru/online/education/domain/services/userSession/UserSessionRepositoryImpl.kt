package ru.online.education.domain.services.userSession

import model.SessionState
import model.UserSession
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.update
import repository.UserSessionRepository
import ru.online.education.data.table.UserSessionTable
import ru.online.education.data.table.UsersTable
import ru.online.education.di.dbQuery
import java.util.*

class UserSessionRepositoryImpl : UserSessionRepository {
    override suspend fun checkSession(id: String): Boolean = getById(id)?.state == SessionState.Started

    override suspend fun getAll(page: Int): List<UserSession> = dbQuery {
        UserSessionTable
            .selectAll()
            .limit(n = pageSize, offset = (page * pageSize).toLong())
            .map(::resultRowToUserSession)
    }

    override suspend fun getById(id: String): UserSession? =
        dbQuery {
            UserSessionTable
                .selectAll()
                .where { UserSessionTable.id eq UUID.fromString(id) }
                .singleOrNull()
                ?.toUserSession()
        }

    override suspend fun deleteById(id: String): Unit =
        dbQuery {
            UserSessionTable
                .update({ UserSessionTable.id eq UUID.fromString(id) }) {
                    it[state] = SessionState.Ended
                }
        }

    override suspend fun update(data: UserSession): UserSession? {
        TODO("Not yet implemented")
    }

    override suspend fun add(data: UserSession): String? = dbQuery {
        UserSessionTable.insertAndGetId {
            userToInsertStatement(it, data)
        }.value.toString()
    }

    private fun resultRowToUserSession(row: ResultRow) = row.toUserSession()
    private fun ResultRow.toUserSession() = UserSession(
        id = this[UserSessionTable.id].value.toString(),
        userId = this[UserSessionTable.user].value,
        lastOnline = this[UserSessionTable.lastOnline],
        authDate = this[UserSessionTable.authDate],
        state = this[UserSessionTable.state],
        host = this[UserSessionTable.host],
    )

    private fun <T : Any> UserSessionTable.userToInsertStatement(statement: InsertStatement<T>, session: UserSession) {
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