package ru.online.education.data.table

import org.jetbrains.exposed.crypt.encryptedVarchar
import org.jetbrains.exposed.dao.id.IntIdTable
import ru.online.education.di.entityEncryptor

object UserRoleTable : IntIdTable() {
    val name = encryptedVarchar("name", 30, entityEncryptor)
}
