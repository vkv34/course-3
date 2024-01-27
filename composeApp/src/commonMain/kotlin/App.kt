import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.ui.window.Popup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.PopupProperties
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
@Composable
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        val greeting = remember { Greeting().greet() }
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = { showContent = !showContent }) {
                Text("Click me!")
            }
            DropdownMenu(
                expanded = showContent,
                onDismissRequest = {
                    showContent = false
                },
                properties = PopupProperties(
                    clippingEnabled = true
                ),
            ) {
                DropdownMenuItem(
                    text = {
                           Text("saldkj")
                           },
                    onClick = {
                        
                    }
                )
            }
            if (showContent){
                Popup {
                    DropdownMenuItem(
                        text = {
                            Text("asdasdasdasdasd")
                               },
                        onClick = {

                        }
                    )
                }
            }
            
            Crossfade(showContent){
                if(it){
                    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(painterResource("compose-multiplatform.xml"), null)
                        Text("Compose: $greeting")
                    }
                }
            }
            
            
            
           
            

//            AnimatedVisibility(showContent) {
//                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//                    Image(painterResource("compose-multiplatform.xml"), null)
//                    Text("Compose: $greeting")
//                }
//            }

        }
    }
}