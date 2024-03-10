import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
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
import io.ktor.client.request.*
import kotlinx.browser.document
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import model.*
import org.jetbrains.compose.web.renderComposable
import org.jetbrains.compose.web.renderComposableInBody
import org.w3c.dom.Document
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.ItemArrayLike
import org.w3c.dom.asList
import org.w3c.files.File
import ru.online.education.app.feature.account.presentation.model.AuthScreenState
import ru.online.education.app.feature.account.presentation.ui.AuthScreen
import ru.online.education.app.feature.course.domain.repository.CourseRepositoryImpl
import ru.online.education.app.feature.course.presentation.ui.CoursesScreen
import ru.online.education.app.feature.course.presentation.viewModel.AllCoursesScreenViewModel
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val url = window.location.toString()
    onWasmReady {
        CanvasBasedWindow(canvasElementId = "ComposeTarget") {
//            var token by remember { mutableStateOf("") }
//            val httpCLicent = remember {
//                HttpClient(Js) {
//                    install(ContentNegotiation) {
//                        json(
//                            Json {
//                                ignoreUnknownKeys = true
//                                isLenient = true
//                                serializersModule = SerializersModule {
//                                    polymorphic(Any::class) {
//                                        subclass(AuthResponse::class, AuthResponse.serializer())
//                                        subclass(CourseDto::class, CourseDto.serializer())
//                                        subclass(CourseCategoryDto::class, CourseCategoryDto.serializer())
//
//                                        subclass(ListResponse.serializer(CourseDto.serializer()))
//                                    }
////                polymorphic(Any::class) {
////                    subclass(List::class, ListSerializer(PolymorphicSerializer(Any::class).nullable))
////                }
//
//                                    polymorphic(Image::class) {
//                                        subclass(Image.ImageResource::class, Image.ImageResource.serializer())
//                                        subclass(Image.Color::class, Image.Color.serializer())
//                                    }
//                                }
//                            }
//                        )
//                    }
//                    install(Logging) {
//                        level = LogLevel.ALL
//                    }
//
//                    defaultRequest {
//                        url("http://localhost:8888/")
//                        bearerAuth(token)
//                    }
//                }
//            }
//
//
//            val accountRepositoryImpl = remember {
//                AccountRepositoryImpl(
//                    client = httpCLicent,
//                    authCallback = object : AuthCallback {
//                        override suspend fun onAuth(authResponse: AuthResponse) {
//                            token = authResponse.token
//                        }
//
//                        override suspend fun onUnAuth() {
//
//                        }
//
//                    }
//                )
//            }
//
//            var password by remember { mutableStateOf("") }
//            var email by remember { mutableStateOf("") }
//
//            val corotineScope = rememberCoroutineScope()
//
//            val authScreenState = AuthScreenState(accountRepositoryImpl, corotineScope)
//
//            if (token.isBlank()) {
//                AuthScreen(authScreenState)
//            } else {
//                val courseRepository = remember {
//                    CourseRepositoryImpl(
//                        client = httpCLicent
//                    )
//                }
//                val coursesScreenViewModel = remember {
//                    AllCoursesScreenViewModel(
//                        courseRepository,
//                        corotineScope
//                    )
//                }
//
//                CoursesScreen(
//                    coursesScreenViewModel
//                )
//            }

            App()
//            var showFilePicker by remember { mutableStateOf(false) }
//            var path by remember { mutableStateOf("") }
//
//            val fileType = listOf("jpg", "png")
//            FilePicker(
//                show = showFilePicker,
//                fileExtensions = fileType,
//                initialDirectory = null,
//                title = ""
//            ) { files ->
//                showFilePicker = false
//                // do something with the file
//                path = files.joinToString("\n")
//            }
//
//            Button(
//                onClick = {
//                    showFilePicker = true
//                }
//            ) {
//                Text(path)
//            }

        }
    }
}

@Composable
public fun FilePicker(
    show: Boolean,
    initialDirectory: String?,
    fileExtensions: List<String>,
    title: String?,
    onFileSelected: (List<String>) -> Unit,
) {
    LaunchedEffect(show) {
        if (show) {
            val fixedExtensions = fileExtensions.map { ".$it" }
            val file = document.selectFilesFromDisk(fixedExtensions.joinToString(","), false).map { it.name }
            onFileSelected(file)
        }
    }
}

private suspend fun Document.selectFilesFromDisk(
    accept: String,
    isMultiple: Boolean
): List<File> = suspendCoroutine {
    val tempInput = (createElement("input") as HTMLInputElement).apply {
        type = "file"
        style.display = "none"
        this.accept = accept
        multiple = isMultiple
    }

    tempInput.onchange = { changeEvt ->
        val files = (changeEvt.target.asDynamic().files as ItemArrayLike<File>).asList()
        it.resume(files)
    }

    body!!.append(tempInput)
    tempInput.click()
    tempInput.remove()
}
//
//@OptIn(ExperimentalMaterial3Api::class)
//fun main(){
//    renderComposableInBody{
//        var text by remember {
//            mutableStateOf("")
//        }
//
//
//        var opened by remember {
//            mutableStateOf(false)
//        }
//        Column {
//            TextField(text, onValueChange = {text = it})
//            Button(onClick = {opened = true}){
//                Text("Open")
//            }
//            AlertDialog(onDismissRequest = {
//                opened = false
//            }){
//                Text("")
//            }
//        }
//
//    }
//}