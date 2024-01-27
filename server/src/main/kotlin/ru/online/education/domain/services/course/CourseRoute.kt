package ru.online.education.domain.services.course

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.BaseModel
import model.UserRole
import org.koin.ktor.ext.inject
import ru.online.education.core.util.createAndRespond
import ru.online.education.core.util.getAndRespond
import ru.online.education.core.util.role
import ru.online.education.domain.services.account.auth.jwtAuthenticate
import util.ApiResult

fun Routing.courseRoute() {
    val courseService by application.inject<CourseService>()
    route("/course") {

        jwtAuthenticate {
            role<BaseModel>(UserRole.all)
            get("my/{page}") {
//                val page = call.parameters["page"]?.toInt() ?: 0
//                val result = courseService.getAllCourses(page)
//                if (result is ApiResult.Success) {
//                    call.respond(HttpStatusCode.OK, result)
//                } else {
//                    call.respond(HttpStatusCode.InternalServerError, result)
//                }

                getAndRespond(courseService)
            }
        }

        jwtAuthenticate {
            role<BaseModel>(listOf(UserRole.Teacher, UserRole.Admin))
            post("new") {
//                val course = call.receive<Course>()
//                if (course.isValid) {
//                    val result = courseService.addCourse(course)
//                    call.respond(
//                        status = HttpStatusCode.Created,
//                        result
//                    )
//                } else {
//                    call.respond(
//                        HttpStatusCode.BadRequest,
//                        ApiResult.Error<Course>(message = course.errorsAsString)
//                    )
//
//                }
                createAndRespond(courseService) {
                    copy(id = it)
                }
            }
        }
    }
}