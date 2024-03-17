package destination.root

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import destination.home.HomeContent
import ru.online.education.app.feature.home.AdaptiveScaffold
import ru.online.education.app.feature.home.HorizontalNavigationIcon
import ru.online.education.app.feature.home.model.NavigationItem
import ru.online.education.app.feature.navigation.root.RootComponent

@Composable
fun RootContent(component: RootComponent, modifier: Modifier = Modifier) {
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
            HorizontalNavigationIcon(
                Icons.Default.AccountCircle,
                text = "Аккаунт",
                selected = currentConfig is RootComponent.Child.AccountChild,
                onClick = {
                    component.navigateUp(RootComponent.Config.Account)
                          },
                opened = opened
            ) 
        },
        selected = currentConfig.toConfig().toNavigationItem { }
    ) {
        Child(component = component, modifier = Modifier.fillMaxSize())
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

        }
    }
}

