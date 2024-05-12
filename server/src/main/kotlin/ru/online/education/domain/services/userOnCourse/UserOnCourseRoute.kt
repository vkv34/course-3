package ru.online.education.domain.services.userOnCourse

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.get
import ru.online.education.core.util.respond
import ru.online.education.core.util.respondCreated
import ru.online.education.domain.repository.CourseRepository
import ru.online.education.domain.repository.UserOnCourseRepository
import ru.online.education.domain.repository.model.UserOnCourseDto
import ru.online.education.domain.services.account.auth.jwtAuthenticate
import ru.online.education.domain.services.account.currentUser.getCurrentUser

fun Route.userOnCourseRoute() {
    val userOnCourseRepository = application.get<UserOnCourseRepository>()
//    val courseRepository = application.get<CourseRepository>()
    route("userOnCourse") {
        jwtAuthenticate {
            get("{id}") {
                val courseId = call.parameters["id"]?.toIntOrNull() ?: 0
                val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 0

                respond(userOnCourseRepository.getUsersOnCourseByCourseId(courseId, page))
            }

            post {
                val userOnCourseDto = call
                    .receive<UserOnCourseDto>()
                    .copy(
                        userDto = getCurrentUser()!!
                    )

                respondCreated(userOnCourseRepository.add(userOnCourseDto))
            }
            put {
                val userOnCourseDto = call.receive<UserOnCourseDto>()

                respond(userOnCourseRepository.update(userOnCourseDto), text = "")
            }

            delete("{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: 0

                respond(userOnCourseRepository.deleteById(id))
            }
        }
    }
}