package navgation.destination.root.home

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp

@Composable
fun HomeContent() {
    val localUriHandler = LocalUriHandler.current
    Card(
        modifier = Modifier.padding(16.dp),
        onClick = {
            localUriHandler.openUri("https://knowedge.ru/app.apk")
        }
    ) {
        Text(
            text = "Скачайте наше приложение для os Android!",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Нажимте для скачивания",
            style = MaterialTheme.typography.headlineLarge
        )
    }
}