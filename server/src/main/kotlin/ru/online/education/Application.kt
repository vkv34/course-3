package ru.online.education

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import model.*
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

fun main(args: Array<String>) = io.ktor.server.netty.EngineMain.main(args)

/*{
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}*/

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
            serializersModule = SerializersModule {
                polymorphic(Any::class) {
                    subclass(LoginResponse::class, LoginResponse.serializer())
                    subclass(Course::class, Course.serializer())
                    subclass(CourseCategoryDto::class, CourseCategoryDto.serializer())

                    subclass(ListResponse.serializer(Course.serializer()))
                }
//                polymorphic(Any::class) {
//                    subclass(List::class, ListSerializer(PolymorphicSerializer(Any::class).nullable))
//                }

                polymorphic(Image::class) {
                    subclass(Image.ImageResource::class, Image.ImageResource.serializer())
                    subclass(Image.Color::class, Image.Color.serializer())
                }
            }
        })
    }

    installJWTAuth()

    installTelemetry()


    val userService by inject<UserService>()
    val dataBase by inject<Database>()
    routing {
        installAccountRoute()

        courseRoute()

        courseCategoryRoute()

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
