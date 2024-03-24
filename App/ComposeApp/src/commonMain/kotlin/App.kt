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
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import ru.online.education.app.feature.account.domain.repository.AuthCallback
import ru.online.education.app.feature.account.domain.repository.impl.AccountRepositoryImpl
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import model.*
import ru.online.education.app.feature.account.presentation.model.AuthScreenState
import ru.online.education.app.feature.account.presentation.ui.AuthScreen
import ru.online.education.app.feature.course.domain.repository.CourseRepositoryImpl
import ru.online.education.app.feature.course.presentation.viewModel.AllCoursesScreenViewModel
import ru.online.education.app.feature.home.AdaptiveScaffold
import ru.online.education.app.feature.home.model.NavigationGroup
import ru.online.education.app.feature.home.model.NavigationItem
import kotlin.random.Random

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
@Composable
fun App() {
    MaterialTheme {

    }
}