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
import ru.online.education.app.feature.course.presentation.ui.CoursesScreen
import ru.online.education.app.feature.course.presentation.viewModel.AllCoursesScreenViewModel
import ru.online.education.app.feature.home.AdaptiveScaffold
import ru.online.education.app.feature.home.model.NavigationGroup
import ru.online.education.app.feature.home.model.NavigationItem
import kotlin.random.Random

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
@Composable
fun App() {
    MaterialTheme {
        var token by remember { mutableStateOf("") }
        val httpCLicent = remember {
            HttpClient() {
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
                    url("http://localhost:8888/")
                    bearerAuth(token)
                }
            }
        }


        val accountRepositoryImpl = remember {
            AccountRepositoryImpl(
                client = httpCLicent,
                authCallback = object : AuthCallback {
                    override suspend fun onAuth(authResponse: AuthResponse) {
                        token = authResponse.token
                    }

                    override suspend fun onUnAuth() {

                    }

                }
            )
        }

        var password by remember { mutableStateOf("rtwerwerwerwe") }
        var email by remember { mutableStateOf("") }

        val corotineScope = rememberCoroutineScope()

        val authScreenState = AuthScreenState(accountRepositoryImpl, corotineScope)

        var showFilePicker by remember { mutableStateOf(false) }
        var path by remember { mutableStateOf("") }

        val fileType = listOf("jpg", "png")
//        FilePicker(
//            show = showFilePicker,
//            fileExtensions = fileType,
//            initialDirectory = null,
//        ) { platformFile ->
//            showFilePicker = false
//            // do something with the file
//            path = platformFile?.path ?: ""
//        }

        //        Column {
        //
        //
        //            Button(
        //                onClick = {
        //                    showFilePicker = true
        //                }
        //            ) {
        //                Text(path)
        //            }
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
        //
        //        }
        //
        //    }
//        val navItems = listOf(
//            NavigationItem(
//                name = "alkfja",
//                icon = Icons.Default.Home,
//                selected = false
//            ) {},
//            NavigationItem(
//                name = "sadsadasd",
//                icon = Icons.Default.Refresh,
//                selected = false,
//                navigationGroup = NavigationGroup.accountGroup
//
//            ) {},
//            NavigationItem(
//                name = ",.cnbkjfh",
//                icon = Icons.Default.DeleteForever,
//                selected = true,
//                navigationGroup = NavigationGroup.menuGroup
//            ) {},
//
//            )
//
//        AdaptiveScaffold(
//            navigationItems = navItems
//        ) {
//            val a = rememberSaveable { Random.nextBits(10).toString() }
//            Text(a)
//        }
    }
}