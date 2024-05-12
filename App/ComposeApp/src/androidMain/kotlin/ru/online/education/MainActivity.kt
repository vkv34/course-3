package ru.online.education

import App
import ApplicationTheme
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import navgation.destination.root.RootContent
import root.RootComponent
import ru.online.education.app.core.util.coruotines.DispatcherProvider

//import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalDecomposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = RootComponent(
            context = DefaultComponentContext(lifecycle),
            startDestination = RootComponent.Config.Courses
        )

        lifecycleScope.launch {
            root.notificationManager.notificationFlow().collect { messages ->
                val message = messages.firstOrNull()
                if (message != null) {
                    withContext(Dispatchers.Main) {
                        Toast
                            .makeText(this@MainActivity, message.title, Toast.LENGTH_SHORT)
                            .show()
                    }
                }

            }
        }
        setContent {
            ApplicationTheme {
                RootContent(
                    component = root,
                    modifier = Modifier.fillMaxSize(),

                )
            }

        }
    }
}

//@Preview
//@Composable
//fun AppAndroidPreview() {
//    App()
//}