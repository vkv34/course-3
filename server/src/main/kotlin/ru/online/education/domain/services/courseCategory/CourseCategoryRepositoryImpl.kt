package ru.online.education.domain.services.courseCategory

import model.CourseCategoryDto
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.InsertStatement
import repository.CourseCategoryRepository
import ru.online.education.data.table.CourseCategoryTable
import ru.online.education.di.dbQuery

class CourseCategoryRepositoryImpl : CourseCategoryRepository {
    override suspend fun getAll(page: Int): List<CourseCategoryDto> = dbQuery {
        CourseCategoryTable
            .selectAll()
            .limit(pageSize, (page * pageSize).toLong())
            .map(::resultRowToCourse)
    }

    override suspend fun getById(id: Int): CourseCategoryDto? = dbQuery {
        CourseCategoryTable
            .selectAll()
            .where { CourseCategoryTable.id eq id }
            .singleOrNull()
            ?.toCourseCategory()
    }

    override suspend fun deleteById(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun update(data: CourseCategoryDto): CourseCategoryDto? {
        TODO("Not yet implemented")
    }

    override suspend fun add(data: CourseCategoryDto): Int? = dbQuery {
        CourseCategoryTable
            .insertAndGetId {
                courseCategoryToInsertStatement(it, data)
            }.value
    }

    private fun resultRowToCourse(row: ResultRow) = row.toCourseCategory()
    private fun ResultRow.toCourseCategory() = CourseCategoryDto(
        id = this[CourseCategoryTable.id].value,
        name = this[CourseCategoryTable.name],
        description = this[CourseCategoryTable.description]
    )

    private fun <T : Any> courseCategoryToInsertStatement(
        statement: InsertStatement<T>,
        courseCategory: CourseCategoryDto
    ) {
        with(courseCategory) {
            statement[CourseCategoryTable.name] = name
            statement[CourseCategoryTable.description] = description
        }

    }
}