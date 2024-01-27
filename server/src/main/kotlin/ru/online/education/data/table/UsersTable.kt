package ru.online.education.data.table

import org.jetbrains.exposed.crypt.EncryptedVarCharColumnType
import org.jetbrains.exposed.crypt.encryptedVarchar
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import ru.online.education.di.entityEncryptor
import ru.online.education.di.passwordEnryptor

object UsersTable : IntIdTable(name = "User") {
    val email = encryptedVarchar("email", 100, entityEncryptor)
        .uniqueIndex()

    val password = encryptedVarchar("password", 100, entityEncryptor)
    val firstName = encryptedVarchar("firstName", 100, entityEncryptor)
    val secondName = encryptedVarchar("secondName", 100, entityEncryptor)
    val lastName = encryptedVarchar("lastName", 100, entityEncryptor)

    val userRole =
        reference(
            name = "roleId",
            foreign = UserRoleTable,
            onDelete = ReferenceOption.CASCADE,
            fkName = "FK_User_Role"
        ).default(EntityID(1, UserRoleTable))

}