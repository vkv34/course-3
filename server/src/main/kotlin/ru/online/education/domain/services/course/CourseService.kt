package ru.online.education.domain.services.course

import ru.online.education.domain.repository.model.CourseDto
import ru.online.education.domain.repository.model.ListResponse
import ru.online.education.domain.repository.CourseRepository
import ru.online.education.core.util.apiCall
import ru.online.education.domain.model.BaseService
import util.ApiResult

class CourseService(
    val courseRepository: CourseRepository,
) : BaseService<CourseDto, Int> {
//    suspend fun getAllCourses(page: Int): ApiResult<List<Course>> = try {
//        ApiResult.Success(courseRepository.getAll(page))
//    } catch (e: Exception) {
//        ApiResult.Error(e.localizedMessage)
//    }

    suspend fun addCourse(course: CourseDto) = courseRepository.add(course)

    suspend fun getCourseById(id: Int) =
        apiCall {
            courseRepository.getById(id)
        }

    override suspend fun create(data: CourseDto): ApiResult<CourseDto> =
        apiCall {
            courseRepository.add(data)
        }

    override suspend fun update(data: CourseDto): ApiResult<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun getAll(page: Int): ApiResult<ListResponse<CourseDto>> =
        apiCall {
            courseRepository.getAll(page)
        }

    override suspend fun getById(id: Int): ApiResult<CourseDto> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteById(id: Int): ApiResult<Int> {
        TODO("Not yet implemented")
    }

    suspend fun getAllByUserId(
        userId: Int,
        page: Int,
    ) = courseRepository.filterByUser(page = page, userId = userId)
}
