package ru.online.education.domain.services.publicationAnswer

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.get
import ru.online.education.core.util.respond
import ru.online.education.core.util.respondCreated
import ru.online.education.core.util.role
import ru.online.education.domain.repository.model.BaseModel
import ru.online.education.domain.repository.model.PublicationAnswerDto
import ru.online.education.domain.repository.model.UserRole
import ru.online.education.domain.services.account.auth.jwtAuthenticate
import ru.online.education.domain.services.account.currentUser.getCurrentUser

fun Route.publicationAnswerRoute() {
    val repository = PublicationAnswerRepositoryImpl(application.get())
    route("publicationAnswer") {
        jwtAuthenticate {
            role<BaseModel>(UserRole.entries)

            get("{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: 0

                respond(repository.getById(id))
            }

            get("byPublicationOnCourseId/{page}") {
                val publicationOnCourseId = call.request.queryParameters["publicationOnCourseId"]?.toIntOrNull() ?: 0
                val userId = call.request.queryParameters["userId"]?.toIntOrNull() ?: 0
                val page = call.parameters["page"]?.toIntOrNull() ?: 0

                if (userId == 0) {
                    respond(
                        repository.getAllByPublicationOnCourseId(
                            publicationOnCourseId = publicationOnCourseId,
                            page = page
                        )
                    )
                } else {
                    respond(
                        repository.getAllByPublicationOnCourseId(
                            publicationOnCourseId = publicationOnCourseId,
                            userId = userId,
                            page = page
                        )
                    )
                }
            }

            post {
                val publicationAnswerDto = call.receive<PublicationAnswerDto>()
                val currentUser = getCurrentUser() ?: return@post


                respondCreated(
                    repository.add(
                        publicationAnswerDto.copy(
                            userId = currentUser.id
                        )
                    )
                )
            }

            put {
                val publicationAnswerDto = call.receive<PublicationAnswerDto>()

                respond(repository.update(publicationAnswerDto), text = "")
            }

            delete("{id}") {
                val id = call.request.queryParameters["id"]?.toIntOrNull() ?: 0

                respond(repository.deleteById(id))
            }
        }
    }
}