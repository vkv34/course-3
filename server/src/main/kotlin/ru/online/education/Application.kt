package ru.online.education

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import org.koin.core.logger.Level
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import ru.online.education.di.appModule
import ru.online.education.di.dataBaseModule
import ru.online.education.di.getJwtModule
import ru.online.education.domain.services.account.auth.installAccountRoute
import ru.online.education.domain.services.account.auth.installJWTAuth
import ru.online.education.domain.services.course.courseRoute
import ru.online.education.domain.services.courseCategory.courseCategoryRoute
import ru.online.education.domain.services.telemetry.installTelemetry
import ru.online.education.domain.services.userService.UserService
import io.ktor.server.plugins.cors.routing.*
import ru.online.education.domain.services.coursePublication.coursePublicationRoute
import ru.online.education.domain.services.publicationAttachment.publicationAttachmentRoute
import ru.online.education.domain.services.userService.userRoute

fun main(args: Array<String>) = io.ktor.server.netty.EngineMain.main(args)

//    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
//        .start(wait = true)

fun Application.module() {



    install(Koin) {
        slf4jLogger(Level.DEBUG)
        modules(
            appModule,
            dataBaseModule,
            getJwtModule(this@module)
        )
    }

    /*  install(StatusPages){
          exception<Throwable>{

          }
      }*/

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            serializersModule = api.defaultSerializersModule
        })
    }

    install(CORS) {
//        allowHost("localhost:8080")
        anyHost()
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Put)
    }

    installJWTAuth()

    installTelemetry()


    val userService by inject<UserService>()
    val dataBase by inject<Database>()
    routing {
        installAccountRoute()

        courseRoute()

        courseCategoryRoute()

        coursePublicationRoute()

        publicationAttachmentRoute()

       /* openAPI(path="openapi", swaggerFile = "openapi/documentation.yaml") {
            codegen = StaticHtmlCodegen()
        }*/

        userRoute()

        get("/error") {
            logError(call, Exception("Test Error"))
            call.respond("error")
        }

        get("/") {
            call.response.header(HttpHeaders.Connection, "keep-alive")
            call.respond(status = HttpStatusCode.BadGateway, "asdasd")
            delay(1000)
            call.respond(status = HttpStatusCode.BadGateway, "asdasdasdasdasdasdasdasd")
        }

        post("/") {

        }
    }
}
