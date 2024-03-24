package ru.online.education.domain.services.coursePublication

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import model.PublicationDto
import model.PublicationOnCourseDto
import model.UserRole
import ru.online.education.core.util.*
import ru.online.education.domain.services.account.auth.jwtAuthenticate
import ru.online.education.domain.services.account.currentUser.getCurrentUser
import util.ApiResult

fun Routing.coursePublicationRoute() {

    route("/course/") {
        jwtAuthenticate {
            role<PublicationOnCourseDto>(UserRole.all)

            get("{courseId}/publication/{page}") {
                val courseId = call.parameters["courseId"]?.toInt() ?: 0
                val page = call.parameters["page"]?.toInt() ?: 0
                val currentUser =
                    getCurrentUser()
                if (currentUser == null)
                    respond(ApiResult.Error<PublicationOnCourseDto>("Ошибка при авторизации"))
                checkNotNull(currentUser)
                getAndRespond {
                    PublicationRepositoryImpl(
                        currentUser.role
                    ).getByCourseId(courseId, page)
                }
            }

            post("{courseId}/publication/new") {
                val courseId = call.parameters["courseId"]?.toInt() ?: 0
                val currentUser =
                    getCurrentUser()
                if (currentUser == null)
                    respond(ApiResult.Error<PublicationDto>("Ошибка при авторизации"))
                checkNotNull(currentUser)
                val publication = call.receive<PublicationDto>()
                    .copy(
                        courseId = courseId,
                        authorId = currentUser.id
                    )
                validateInput(publication)
                val publicationRepository = PublicationRepositoryImpl(
                    currentUser.role
                )
                val publicationOnCourseRepository = PublicationOnCourseRepositoryImpl()

                val addedPublication = publicationRepository.add(publication)
                if (addedPublication !is ApiResult.Success)
                    respond(addedPublication)
                check(addedPublication is ApiResult.Success)
                val addedPublicationOnCourse = publicationOnCourseRepository.add(
                    PublicationOnCourseDto(
                        publicationId = addedPublication.data.publicationId,
                        courseId = courseId,
                        userId = currentUser.id,
                        visible = addedPublication.data.visible,
                        temp = addedPublication.data.temp
                    )
                )

                if (addedPublicationOnCourse !is ApiResult.Success)
                    respond(addedPublicationOnCourse)
                check(addedPublicationOnCourse is ApiResult.Success)

                respondCreated(
                    ApiResult.Success(
                        addedPublication.data.copy(
                            courseId = courseId,
                            publicationInCourseId = addedPublicationOnCourse.data.id,
                            authorId = currentUser.id
                        ),
                        message = "Публикация добавлена"
                    )
                )

            }


        }
    }
}