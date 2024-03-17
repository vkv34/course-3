import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import destination.root.RootContent
import ru.online.education.app.composeApp.runOnUiThread
import ru.online.education.app.feature.navigation.root.RootComponent

@OptIn(ExperimentalDecomposeApi::class)
fun main() {
    val lifecycle = LifecycleRegistry()

    val root =
        runOnUiThread {
            RootComponent(
                context = DefaultComponentContext(
                    lifecycle = lifecycle,
                    //                    stateKeeper = stateKeeper,
                ),
            )
        }


    application {

        val windowState = rememberWindowState()

        LifecycleController(lifecycle, windowState)

//        var isCloseRequested by remember { mutableStateOf(false) }

        Window(onCloseRequest = ::exitApplication, title = "Online Education") {
            RootContent(
                component = root
            )
        }
    }
}

@Preview
@Composable
fun AppDesktopPreview() {
    App()
}