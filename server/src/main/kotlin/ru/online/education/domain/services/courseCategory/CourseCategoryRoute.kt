package ru.online.education.domain.services.courseCategory

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.online.education.domain.repository.model.BaseModel
import ru.online.education.domain.repository.model.UserRole
import org.koin.ktor.ext.inject
import ru.online.education.domain.repository.CourseCategoryRepository
import ru.online.education.core.util.createAndRespond
import ru.online.education.core.util.respond
import ru.online.education.core.util.role
import ru.online.education.domain.services.account.auth.jwtAuthenticate

fun Routing.courseCategoryRoute() {
    val courseCategoryService by application.inject<CourseCategoryService>()
    val courseCategoryRepository by application.inject<CourseCategoryRepository>()
    route("/courseCategory") {
        jwtAuthenticate {
//            role<BaseModel>(listOf(UserRole.Admin, UserRole.Teacher, UserRole.Moderator))
            post("new") {
                createAndRespond(courseCategoryService)
            }

            get("all/{page}") {
                val page = call.parameters["page"]?.toInt() ?: 0

                respond(courseCategoryService.getAll(page))
            }

            get("{id}") {
                val id = call.parameters["id"]?.toInt() ?: 0
                respond(courseCategoryRepository.getById(id))
            }

            get("") {
                val search = call.request.queryParameters["search"] ?: ""
                val page = call.request.queryParameters["page"]?.toInt() ?: 0

                respond(courseCategoryRepository.findByName(search, page))
            }
        }
    }
}
