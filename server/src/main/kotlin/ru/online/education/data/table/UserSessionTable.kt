package ru.online.education.data.table

import model.SessionState
import org.jetbrains.exposed.crypt.encryptedVarchar
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import ru.online.education.di.entityEncryptor

object UserSessionTable : UUIDTable() {
    val user = reference("user", UsersTable)
    val authDate = datetime("authDate").defaultExpression(CurrentDateTime)
    val lastOnline = datetime("lastOnline").defaultExpression(CurrentDateTime)
    val state = enumeration<SessionState>("state")
    val host = encryptedVarchar("host", 500, entityEncryptor)
}