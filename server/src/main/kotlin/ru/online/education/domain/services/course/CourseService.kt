package ru.online.education.domain.services.course

import model.Course
import model.ListResponse
import repository.CourseRepository
import ru.online.education.core.util.apiCall
import ru.online.education.domain.model.BaseService
import util.ApiResult

class CourseService(
    val courseRepository: CourseRepository
) : BaseService<Course, Int> {
//    suspend fun getAllCourses(page: Int): ApiResult<List<Course>> = try {
//        ApiResult.Success(courseRepository.getAll(page))
//    } catch (e: Exception) {
//        ApiResult.Error(e.localizedMessage)
//    }

    suspend fun addCourse(course: Course) = apiCall(
        successMessage = "Курс успешно добавлен",
        errorMessage = "Ошибка при добавленнии курса"
    ) {
        val id = courseRepository.add(course) ?: error("Курс не добавлен")
        getCourseById(id)
    }

    suspend fun getCourseById(id: Int) = apiCall(
        errorMessage = "Курс не найден"
    ) {
        courseRepository.getById(id)
    }

    override suspend fun create(data: Course): ApiResult<Int> = apiCall(
        successMessage = "Курс успешно добавлен",
        errorMessage = "Ошибка при добавленнии курса"
    ) {
        courseRepository.add(data) ?: error("Курс не добавлен")
    }

    override suspend fun update(data: Course): ApiResult<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun getAll(page: Int): ApiResult<ListResponse<Course>> = apiCall(
        errorMessage = "Ошибка при загрузке курсов",
        successMessage = "Курсы загружены"
    ) {
        ListResponse(
            courseRepository.getAll(page)
        )
    }

    override suspend fun getById(id: Int): ApiResult<Course> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteById(id: Int): ApiResult<Int> {
        TODO("Not yet implemented")
    }
}