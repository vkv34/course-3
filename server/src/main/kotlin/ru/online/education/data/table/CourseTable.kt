package ru.online.education.data.table

import kotlinx.datetime.LocalDateTime
import ru.online.education.domain.repository.model.CourseState
import org.jetbrains.exposed.crypt.encryptedVarchar
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import ru.online.education.di.entityEncryptor
import java.math.BigDecimal

object CourseTable : IntIdTable(name = "course") {
    val name = encryptedVarchar(name = "name", 250, entityEncryptor)
    val courseCategory = reference("FK_CourseCategory", CourseCategoryTable, onDelete = ReferenceOption.CASCADE)
    val shortDescription = encryptedVarchar(name = "shortDescription", 250, entityEncryptor)
    val longDescription = encryptedVarchar(name = "longDescription", 250, entityEncryptor)
    val dateCreate = datetime("dateCreate").default(LocalDateTime(2024, 1, 1, 0, 0, 0))
    val courseState = enumeration<CourseState>("courseState").default(CourseState.Moderation)
    val color = varchar("color", 250).default("")
    val avatar = varchar("avatar", 1000).default("")

    val createdBy = reference("createdBy", UsersTable, onDelete = ReferenceOption.SET_NULL).nullable()

    val teacherCount = integer("teacherCount").default(1)
    val studentCount = integer("studentCount").default(0)
    val rating = decimal("rating", 1, 1).default(BigDecimal(0))
}
