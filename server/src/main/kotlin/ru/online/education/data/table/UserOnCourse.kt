package ru.online.education.data.table

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object UserOnCourse: IntIdTable() {
    val user = reference("user", UsersTable, onDelete = ReferenceOption.CASCADE)
    val role = reference("userRoleOnCourse", UserRoleTable, onDelete = ReferenceOption.CASCADE)
    val course = reference("course", CourseTable, onDelete = ReferenceOption.CASCADE)
}