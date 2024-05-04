import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
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
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.*
import navgation.destination.root.RootContent
import org.jetbrains.skiko.wasm.onWasmReady
import org.w3c.dom.Document
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.ItemArrayLike
import org.w3c.dom.asList
import org.w3c.files.File
import root.RootComponent
import ru.online.education.app.core.util.coruotines.DispatcherProvider
import ru.online.education.app.feature.di.instalDi
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalComposeUiApi::class, ExperimentalDecomposeApi::class)
fun main() {
//    val url = window.location.toString()

    val launchDelay = 1.seconds
    val lifecycle = LifecycleRegistry()



//    addEndPointsToServiceWorker(rootEndPoints)

    instalDi()

    val root = RootComponent(
        context = DefaultComponentContext(lifecycle),
        deepLink = DeepLink.Web(window.location.pathname),
        webHistoryController = DefaultWebHistoryController()
    )

    val scope = CoroutineScope(SupervisorJob() + DispatcherProvider.IO)

    scope.launch {
        root.notificationManager.notificationFlow().collect { messages ->
            val message = messages.firstOrNull()
            if (message != null) {
                withContext(Dispatchers.Main) {
                    window.alert("${message.title} \n ${message.content}")
                }
            }
        }
    }


    window.onload = { event ->
        val element = window.document.createElement("canvas")
        element.id = "ComposeTarget"
        document.body?.appendChild(element) ?: window.alert("body null")
        onWasmReady {


            CanvasBasedWindow(canvasElementId = element.id) {
                val coroutineScope = rememberCoroutineScope()
                var showLoadingScreen by remember { mutableStateOf(true) }
                LaunchedEffect(Unit) {
                    coroutineScope.launch {
                        delay(launchDelay)
                    }
                    showLoadingScreen = false
                }
                ApplicationTheme {
                    Crossfade(showLoadingScreen) {
                        if (it) {
                            Box(Modifier.fillMaxSize()) {
                                CircularProgressIndicator()
                            }
                        } else {
                            RootContent(
                                component = root,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }

                WindowExceptionHandler {
                    console.log(it)
                }
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