import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.CanvasBasedWindow
import androidx.compose.ui.window.WindowExceptionHandler
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.webhistory.DefaultWebHistoryController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import deepLinking.DeepLink
import destination.root.RootContent
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
import org.w3c.dom.*
import org.w3c.files.File
import ru.online.education.app.feature.account.presentation.model.AuthScreenState
import ru.online.education.app.feature.account.presentation.ui.AuthScreen
import ru.online.education.app.feature.course.domain.repository.CourseRepositoryImpl
import ru.online.education.app.feature.course.presentation.ui.CoursesScreen
import ru.online.education.app.feature.course.presentation.viewModel.AllCoursesScreenViewModel
import ru.online.education.app.feature.navigation.root.RootComponent
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalComposeUiApi::class, ExperimentalDecomposeApi::class)
fun main() {
//    val url = window.location.toString()
    val lifecycle = LifecycleRegistry()

    val root = RootComponent(
        context = DefaultComponentContext(lifecycle),
        deepLink = DeepLink.Web(window.location.pathname),
        webHistoryController = DefaultWebHistoryController()
    )

    addEndPointsToServiceWorker(rootEndPoints)


    window.onload = { event ->
        val element = window.document.createElement("canvas")
        element.id = "ComposeTarget"
        document.body?.appendChild(element) ?: window.alert("body null")
        onWasmReady {

            CanvasBasedWindow(canvasElementId = element.id) {
                WindowExceptionHandler {
                    console.log(it)
                }
                RootContent(
                    component = root,
                    modifier = Modifier.fillMaxSize()
                )
            }
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