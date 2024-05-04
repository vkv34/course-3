package ru.online.education.data.table

import ru.online.education.domain.repository.model.PublicationCategory
import org.jetbrains.exposed.crypt.encryptedVarchar
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import ru.online.education.di.entityEncryptor

object PublicationTable : IntIdTable() {
    val title = encryptedVarchar("title", 250, entityEncryptor)
    val subTitle = encryptedVarchar("subTitle", 500, entityEncryptor)
    val content = encryptedVarchar("content", 10000, entityEncryptor)
    val author = reference("author", UsersTable, onDelete = ReferenceOption.CASCADE)
    val type = enumeration<PublicationCategory>("type")
}
