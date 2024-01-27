package ru.online.education.domain.services.course

import model.Course
import model.Image
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.InsertStatement
import repository.CourseRepository
import ru.online.education.data.table.CourseCategoryTable
import ru.online.education.data.table.CourseTable
import ru.online.education.data.table.UsersTable
import ru.online.education.di.dbQuery

class CourseRepositoryImpl : CourseRepository {
    override suspend fun findByName(name: String): Course {
        TODO("Not yet implemented")
    }

    override suspend fun filterByUser(userId: Int, page: Int): List<Course> = dbQuery {
        CourseTable.selectAll()
            .where { CourseTable.createdBy eq userId }
            .limit(pageSize, (page * pageSize).toLong())
            .map(::resultRowToCourse)
    }

    override suspend fun getAll(page: Int): List<Course> = dbQuery {
        CourseTable.selectAll()
            .limit(pageSize, (page * pageSize).toLong())
            .map(::resultRowToCourse)
    }

    override suspend fun getById(id: Int): Course? = dbQuery {
        CourseTable.selectAll()
            .where { CourseTable.id eq id }
            .singleOrNull()
            ?.toCourse()
    }

    override suspend fun deleteById(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun update(data: Course): Course? {
        TODO("Not yet implemented")
    }

    override suspend fun add(data: Course): Int? = dbQuery {
        CourseTable.insertAndGetId {
            courseToInsertStatement(it, data)
        }.value
    }

    private fun resultRowToCourse(row: ResultRow) = row.toCourse()
    private fun ResultRow.toCourse() = Course(
        id = this[CourseTable.id].value,
        name = this[CourseTable.name],
        creatorId = this[CourseTable.createdBy]?.value,
        shortDescription = this[CourseTable.shortDescription],
        longDescription = this[CourseTable.longDescription],
        dateCreate = this[CourseTable.dateCreate],
        avatar = Image.ImageResource(this[CourseTable.avatar]),
        background = Image.ImageResource(this[CourseTable.color]),
        courseCategoryId = this[CourseTable.courseCategory].value
    )

    private fun <T : Any> courseToInsertStatement(statement: InsertStatement<T>, course: Course) {
        with(course) {
            statement[CourseTable.name] = name
            if (creatorId != null)
                statement[CourseTable.createdBy] = EntityID(creatorId!!, UsersTable)
            statement[CourseTable.courseCategory] = EntityID(courseCategoryId, CourseCategoryTable)
            statement[CourseTable.shortDescription] = shortDescription
            statement[CourseTable.longDescription] = longDescription
            statement[CourseTable.dateCreate] = dateCreate
            statement[CourseTable.avatar] = (avatar as? Image.ImageResource)?.src ?: ""
            statement[CourseTable.color] = (background as? Image.ImageResource)?.src ?: ""
        }

    }
}