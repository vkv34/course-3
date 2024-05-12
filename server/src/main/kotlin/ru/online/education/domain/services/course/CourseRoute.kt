package ru.online.education.domain.services.course

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ru.online.education.core.util.respond
import ru.online.education.core.util.respondCreated
import ru.online.education.core.util.role
import ru.online.education.core.util.validateInput
import ru.online.education.domain.repository.CourseRepository
import ru.online.education.domain.repository.model.BaseModel
import ru.online.education.domain.repository.model.CourseDto
import ru.online.education.domain.repository.model.UserRole
import ru.online.education.domain.services.account.auth.jwtAuthenticate
import ru.online.education.domain.services.account.currentUser.getCurrentSession
import util.map

fun Routing.courseRoute() {
    val courseService by application.inject<CourseService>()
    val courseRepository by application.inject<CourseRepository>()

    route("/course") {
        jwtAuthenticate {
            role<BaseModel>(UserRole.all)
            get("my/{page}") {
                val currentSession = getCurrentSession()
                val page = call.parameters["page"]?.toInt() ?: 0
                val archived = call.request.queryParameters["archived"]?.toBooleanStrictOrNull() ?: false
                respond(courseService.getAllByUserId(currentSession.userId, page, archived = archived))
            }
            post("archive/{courseId}") {
                val currentSession = getCurrentSession()
                val id = call.parameters["courseId"]?.toInt() ?: 0
                val archived = call.request.queryParameters["archived"]?.toBooleanStrictOrNull() ?: false
                respond(courseService.archive(currentSession.userId, archived = archived))
            }
        }

        jwtAuthenticate {
            role<BaseModel>(listOf(UserRole.Teacher, UserRole.Admin))
            post("new") {
                val input =
                    call.receive<CourseDto>().copy(
                        creatorId = getCurrentSession().userId,
                    )
                validateInput(input)
                val result = courseService.create(input)
                respondCreated(
                    result,
                )
            }
        }

        jwtAuthenticate {
//            role<BaseModel>(listOf(UserRole.Teacher, UserRole.Admin))
            get("{id}") {
                val id = call.parameters["id"]?.toInt() ?: 0
                respond(courseService.getCourseById(id))
            }

            put {
                val courseDto = call.receive<CourseDto>()
                respond(courseRepository.update(courseDto).map { it!! })
            }
        }
    }
}
