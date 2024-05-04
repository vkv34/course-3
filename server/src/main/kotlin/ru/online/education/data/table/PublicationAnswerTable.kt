package ru.online.education.data.table

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import ru.online.education.data.table.PublicationAnswerAttachmentTable.defaultExpression

object PublicationAnswerTable : IntIdTable(name = "PublicationAnswer") {

    val user =
        reference(
            name = "user",
            foreign = UsersTable,
            onDelete = ReferenceOption.CASCADE,
        )
    val reviewer =
        reference(
            name = "reviewer",
            foreign = UsersTable,
            onDelete = ReferenceOption.CASCADE,
        ).nullable().default(null)
    val publicationOnCourse =
        reference(
            name = "publicationOnCourse",
            foreign = PublicationOnCourse
        )
    val answer = varchar(
        name = "answer",
        length = 5000
    )
    val mark = byte("mark").nullable().default(null)
    val comment = varchar(name = "comment", length = 5000).nullable().default(null)

    val createdAt = datetime("createdAt").defaultExpression(CurrentDateTime)

}
