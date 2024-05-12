package ru.online.education.domain.repository

import ru.online.education.domain.repository.model.CourseDto
import ru.online.education.domain.repository.model.ListResponse
import ru.online.education.domain.repository.defaults.Repository
import util.ApiResult

interface CourseRepository: Repository<CourseDto, Int> {
    suspend fun findByName(name : String): CourseDto

    suspend fun filterByUser(page: Int, userId: Int = 0, archived: Boolean = false): ApiResult<ListResponse<CourseDto>>

    suspend fun archiveCourse(courseId: Int, archived: Boolean): ApiResult<CourseDto>
}