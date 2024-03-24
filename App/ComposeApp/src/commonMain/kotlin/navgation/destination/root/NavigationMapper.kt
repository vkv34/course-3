package navgation.destination.root

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.School
import ru.online.education.app.feature.home.model.NavigationGroup
import ru.online.education.app.feature.home.model.NavigationItem
import ru.online.education.app.feature.navigation.root.RootComponent

fun RootComponent.Config.toNavigationItem(onClick: (RootComponent.Config) -> Unit) = when (this) {
    RootComponent.Config.Account -> NavigationItem(
        name = "Аккаунт",
        icon = Icons.Default.AccountCircle,
        navigationGroup = NavigationGroup.accountGroup,
        onClick = { onClick(this) }
    )

    RootComponent.Config.Home -> NavigationItem(
        name = "Главная",
        icon = Icons.Default.Home,
        navigationGroup = NavigationGroup.mainGroup,
        onClick = { onClick(this) }
    )

    RootComponent.Config.NotFound -> TODO()
    RootComponent.Config.Courses -> NavigationItem(
        name = "Курсы",
        icon = Icons.Default.School,
        navigationGroup = NavigationGroup.mainGroup,
        onClick = { onClick(this) }
    )
}

