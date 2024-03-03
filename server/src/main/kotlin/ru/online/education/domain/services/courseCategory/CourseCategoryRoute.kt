package ru.online.education.domain.services.courseCategory

import io.ktor.server.routing.*
import model.BaseModel
import model.UserRole
import org.koin.ktor.ext.inject
import ru.online.education.core.util.createAndRespond
import ru.online.education.core.util.role
import ru.online.education.domain.services.account.auth.jwtAuthenticate

fun Routing.courseCategoryRoute() {
    val courseCategoryService by application.inject<CourseCategoryService>()
    route("/courseCategory") {
        jwtAuthenticate {
            role<BaseModel>(listOf(UserRole.Admin, UserRole.Teacher, UserRole.Moderator))
            post("new") {
                createAndRespond(courseCategoryService)
            }
        }
    }

}