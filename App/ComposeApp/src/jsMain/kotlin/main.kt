import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.CanvasBasedWindow
import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.browser.window
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.jetbrains.skiko.wasm.onWasmReady
import ru.online.education.app.feature.account.domain.repository.AuthCallback
import ru.online.education.app.feature.account.domain.repository.impl.AccountRepositoryImpl
import io.ktor.client.plugins.logging.*
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import model.*
import ru.online.education.app.feature.account.presentation.model.AuthScreenState
import ru.online.education.app.feature.account.presentation.ui.AuthScreen

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val url = window.location.toString()
    onWasmReady {
        CanvasBasedWindow(canvasElementId = "ComposeTarget") {
            val httpCLicent = remember {
                HttpClient(Js) {
                    install(ContentNegotiation) {
                        json(
                            Json {
                                ignoreUnknownKeys = true
                                isLenient = true
                                serializersModule = SerializersModule {
                                    polymorphic(Any::class) {
                                        subclass(AuthResponse::class, AuthResponse.serializer())
                                        subclass(CourseDto::class, CourseDto.serializer())
                                        subclass(CourseCategoryDto::class, CourseCategoryDto.serializer())

                                        subclass(ListResponse.serializer(CourseDto.serializer()))
                                    }
//                polymorphic(Any::class) {
//                    subclass(List::class, ListSerializer(PolymorphicSerializer(Any::class).nullable))
//                }

                                    polymorphic(Image::class) {
                                        subclass(Image.ImageResource::class, Image.ImageResource.serializer())
                                        subclass(Image.Color::class, Image.Color.serializer())
                                    }
                                }
                            }
                        )
                    }
                    install(Logging) {
                        level = LogLevel.ALL
                    }

                    defaultRequest {
                        url("http://localhost:8888")
                    }
                }
            }

            var token by remember { mutableStateOf("") }

            val accountRepositoryImpl = remember {
                AccountRepositoryImpl(
                    client = httpCLicent,
                    authCallback = object : AuthCallback {
                        override suspend fun onAuth(authResponse: AuthResponse) {
                            token = authResponse.token
                        }

                        override suspend fun onUnAuth() {
                            TODO("Not yet implemented")
                        }

                    }
                )
            }

            var password by remember { mutableStateOf("") }
            var email by remember { mutableStateOf("") }

            val corotineScope = rememberCoroutineScope()

            val authScreenState = AuthScreenState(accountRepositoryImpl, corotineScope)
            
            AuthScreen(authScreenState)


        }
    }
}