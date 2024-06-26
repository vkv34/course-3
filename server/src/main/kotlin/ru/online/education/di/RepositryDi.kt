package ru.online.education.di

import org.koin.dsl.module
import ru.online.education.data.model.JwtParameters
import ru.online.education.domain.repository.*
import ru.online.education.domain.services.account.AccountRepositoryImpl
import ru.online.education.domain.services.account.AccountService
import ru.online.education.domain.services.course.CourseRepositoryImpl
import ru.online.education.domain.services.course.CourseService
import ru.online.education.domain.services.courseCategory.CourseCategoryRepositoryImpl
import ru.online.education.domain.services.courseCategory.CourseCategoryService
import ru.online.education.domain.services.publicationAnswer.PublicationAnswerRepositoryImpl
import ru.online.education.domain.services.publicationAnswer.atachment.AnswerAttachmentRepositoryImpl
import ru.online.education.domain.services.publicationAttachment.AttachmentRepositoryImpl
import ru.online.education.domain.services.userOnCourse.UserOnCourseRepositoryImpl
import ru.online.education.domain.services.userService.UserRepositoryImpl
import ru.online.education.domain.services.userService.UserService
import ru.online.education.domain.services.userSession.UserSessionRepositoryImpl

val appModule =
    module {
        single<UserRepository> { UserRepositoryImpl() }
        single { UserService(get()) }

        single<UserSessionRepository> { UserSessionRepositoryImpl() }

        single<AccountRepository> {

            val jwtParameters = get<JwtParameters>()

            with(jwtParameters) {
                AccountRepositoryImpl(
                    userRepository = get(),
                    userSessionRepository = get(),
                    secret,
                    issuer,
                    audience,
                )
            }
        }

        single { AccountService(get()) }

        single<CourseRepository> { CourseRepositoryImpl() }
        single { CourseService(courseRepository = get()) }

        single<CourseCategoryRepository> { CourseCategoryRepositoryImpl() }
        single { CourseCategoryService(courseCategoryRepository = get()) }

        single<AttachmentRepository> { AttachmentRepositoryImpl() }
        single<AnswerAttachmentRepository> { AnswerAttachmentRepositoryImpl() }
        single<PublicationAnswerRepository> { PublicationAnswerRepositoryImpl(get()) }
        single<UserOnCourseRepository> {
            UserOnCourseRepositoryImpl(
                userRepository = get()
            )
        }
    }
