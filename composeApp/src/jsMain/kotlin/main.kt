import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.CanvasBasedWindow
import kotlinx.browser.window
import org.jetbrains.skiko.wasm.onWasmReady

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val url = window.location.toString()
    onWasmReady {
        CanvasBasedWindow(canvasElementId = "ComposeTarget") {
            Box(Modifier.fillMaxSize()){
                Text(url)
                App()
            }
        }
    }
}