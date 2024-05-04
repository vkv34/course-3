package ru.online.education.data.table

import ru.online.education.domain.repository.model.PublicationAttachmentType
import org.jetbrains.exposed.crypt.encryptedVarchar
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import ru.online.education.di.entityEncryptor

object PublicationAnswerAttachmentTable : IntIdTable() {
    val name = encryptedVarchar("name", 250, entityEncryptor).default("")
    val type = enumeration<PublicationAttachmentType>("type")
    val content = encryptedVarchar("content", 2500, entityEncryptor)

    val publicationAnswer = reference("publicationAnswer", PublicationAnswerTable).nullable()

    val createdAt = datetime("createdAt").defaultExpression(CurrentDateTime)
}
