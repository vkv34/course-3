package ru.online.education.data.table

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object PublicationOnCourse : IntIdTable() {

    val publication = reference("publication", PublicationTable, onDelete = ReferenceOption.CASCADE)
    val courseTable = reference("course", CourseTable, onDelete = ReferenceOption.CASCADE)
    val user = reference("creator", UsersTable, onDelete = ReferenceOption.CASCADE)

    val visible = bool("visible").default(true)
    val temp = bool("temp").default(false)

    val createdAt = datetime("createdAt").defaultExpression(CurrentDateTime)
    val deadLine = datetime("deadLine").defaultExpression(CurrentDateTime)
}