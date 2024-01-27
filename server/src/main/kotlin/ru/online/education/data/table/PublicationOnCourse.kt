package ru.online.education.data.table

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object PublicationOnCourse : IntIdTable() {

    val publication = reference("publication", PublicationTable, onDelete = ReferenceOption.CASCADE)
    val CourseTable = reference("cource", PublicationTable, onDelete = ReferenceOption.CASCADE)
    val user = reference("creator", UsersTable, onDelete = ReferenceOption.CASCADE)

}