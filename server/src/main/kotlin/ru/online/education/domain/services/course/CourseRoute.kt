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
import ru.online.education.core.util.respond
import ru.online.education.core.util.role
import ru.online.education.domain.services.account.auth.jwtAuthenticate
import ru.online.education.domain.services.account.currentUser.getCurrentSession
import util.ApiResult

fun Routing.courseRoute() {
    val courseService by application.inject<CourseService>()

    route("/course") {

        jwtAuthenticate {
            role<BaseModel>(UserRole.all)
            get("my/{page}") {
                val currentSession = getCurrentSession()
                val page = call.parameters["page"]?.toInt()?:0
                respond(courseService.getAllByUserId(currentSession.userId, page))
            }
        }

        jwtAuthenticate {
            role<BaseModel>(listOf(UserRole.Teacher, UserRole.Admin))
            post("new") {
                createAndRespond(courseService)
            }
        }

    }
}