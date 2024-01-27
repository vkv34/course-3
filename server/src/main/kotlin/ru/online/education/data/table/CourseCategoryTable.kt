package ru.online.education.data.table

import org.jetbrains.exposed.crypt.encryptedVarchar
import org.jetbrains.exposed.dao.id.IntIdTable
import ru.online.education.di.entityEncryptor

object CourseCategoryTable: IntIdTable(name = "CourseCategory") {
    val name = encryptedVarchar("name", 250, entityEncryptor)
        .uniqueIndex("UQ_CourseCategoryName")
    val description = encryptedVarchar("description", 500, entityEncryptor)
}