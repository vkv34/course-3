package navgation.destination.root

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.sample.shared.multipane.CoursesContent
import navgation.destination.root.home.HomeContent
import ru.online.education.app.feature.account.presentation.ui.AuthScreen
import ru.online.education.app.feature.account.presentation.ui.account.AccountCard
import ru.online.education.app.feature.home.AdaptiveScaffold
import ru.online.education.app.feature.home.NaviagtionIcon
import ru.online.education.app.feature.home.model.NavigationItem
import ru.online.education.app.feature.navigation.root.RootComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootContent(component: RootComponent, modifier: Modifier = Modifier) {

    val appState by component.appState.subscribeAsState()

    val navigationItems: List<NavigationItem> = RootComponent.Config.all.map {
        it.toNavigationItem(onClick = component::navigateUp)
    }
    val childStack by component.childStack.subscribeAsState()
    val currentConfig = childStack.active.instance
    AdaptiveScaffold(
        navigationItems,
        quickSettings = { opened ->
//            if (opened) {
//                
//            } else {
//                IconButton(
//                    onClick = {
//                        component.navigateUp(RootComponent.Config.Account)
//                    }
//                ) {
//                    Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null)
//                }
//            }
//            HorizontalNavigationIcon(
//                Icons.Default.AccountCircle,
//                text = "Аккаунт",
//                selected = currentConfig is RootComponent.Child.AccountChild,
//                onClick = {
//                    component.navigateUp(RootComponent.Config.Account)
//                          },
//                opened = opened
//            )
            if (opened) {
                AccountCard(
                    appState.authState,
                    onClick = component::accountClick,
                    onLogOutClick = component::logOutClick
                )
            } else {
                NaviagtionIcon(
                    icon = Icons.Default.ManageAccounts,
                    selected = currentConfig is RootComponent.Child.AccountChild,
                    onClick = {
                        component.accountClick()
                    }
                )
            }
        },
        modifier = modifier,
        selected = currentConfig.toConfig().toNavigationItem { }
    ) {
        Child(component = component, modifier = Modifier.fillMaxSize())
        val dialogComponent by component.dialog.subscribeAsState()
        dialogComponent.child?.instance?.also { authDialogComponent ->
            val authScreenState = authDialogComponent.authScreenState
            val authState = authScreenState.authResult.collectAsState()

            BasicAlertDialog(
                onDismissRequest = {

                },

                ) {
                Card {
                    Box(Modifier.padding(8.dp)) {
                        AuthScreen(
                            authScreenState = authScreenState
                        )
                    }
                }

            }

        }
    }
}

@Composable
fun Child(component: RootComponent, modifier: Modifier) {
    Children(
        stack = component.childStack,
        modifier = Modifier.fillMaxSize(),
        animation = stackAnimation(animator = fade().plus(scale()))
    ) {
        when (val child = it.instance) {
            RootComponent.Child.AccountChild -> Text("This is Account!!!")
            RootComponent.Child.HomeChild -> HomeContent()
            RootComponent.Child.NotFound -> Text("Вы попали не туда")
            is RootComponent.Child.Courses -> CoursesContent(
                component = child.component
            )
        }
    }
}

