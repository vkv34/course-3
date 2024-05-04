package ru.online.education.domain.repository

import ru.online.education.domain.repository.defaults.Repository
import ru.online.education.domain.repository.model.ListResponse
import ru.online.education.domain.repository.model.UserOnCourseDto
import util.ApiResult

interface UserOnCourseRepository: Repository<UserOnCourseDto, Int> {
    suspend fun getUsersOnCourseByCourseId(courseId: Int, page: Int): ApiResult<ListResponse<UserOnCourseDto>>
}