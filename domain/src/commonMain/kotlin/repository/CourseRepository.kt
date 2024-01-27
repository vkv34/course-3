package repository

import model.Course
import repository.defaults.Repository

interface CourseRepository: Repository<Course, Int> {
    suspend fun findByName(name : String): Course

    suspend fun filterByUser(userId: Int, page: Int): List<Course>
}