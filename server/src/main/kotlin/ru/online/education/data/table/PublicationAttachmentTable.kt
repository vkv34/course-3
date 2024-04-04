package ru.online.education.data.table

import model.PublicationAttachmentType
import org.jetbrains.exposed.crypt.encryptedVarchar
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import ru.online.education.di.entityEncryptor

object PublicationAttachmentTable : IntIdTable() {
    val name = encryptedVarchar("name", 250, entityEncryptor).default("")
    val type = enumeration<PublicationAttachmentType>("type")
    val content = encryptedVarchar("content", 2500, entityEncryptor)

    val publication = reference("publicationId", PublicationTable)

    val createdAt = datetime("createdAt").defaultExpression(CurrentDateTime)
}
