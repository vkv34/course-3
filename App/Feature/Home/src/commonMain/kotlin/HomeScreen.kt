package ru.online.education.app.feature.home

//import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.online.education.app.core.util.compose.AdaptiveBox
import ru.online.education.app.core.util.compose.Orienatation
import ru.online.education.app.core.util.compose.debbugable
import ru.online.education.app.core.util.coruotines.DispatcherProvider
import ru.online.education.app.feature.home.model.NavigationGroup
import ru.online.education.app.feature.home.model.NavigationItem


@Composable
fun AdaptiveScaffold(
    navigationItems: List<NavigationItem>,
    selected: NavigationItem,
    modifier: Modifier = Modifier,
    quickSettings: @Composable (opened: Boolean) -> Unit = {},
    content: @Composable () -> Unit
) {
    AdaptiveBox(
        modifier = modifier/*.fillMaxSize()*/
            .debbugable()
    ) { deviceConf ->
        Crossfade(
            targetState = deviceConf.deviceType,
            modifier = Modifier.fillMaxSize()
                .debbugable()
        ) { deviceType ->
            if (deviceType is Orienatation.Horizontal) {
//                NavigationRail{
//                    NavigationRailItem()
//                }
                Box(
                    modifier = Modifier.fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.background)
                ) {

                    Box(
                        modifier = Modifier.fillMaxSize()
                            .padding(start = 60.dp)
                    ) {
                        content()
                    }

                    VerticalNavigationBar(
                        navigationItems = navigationItems,
                        selected = selected,
                        quickSettings = quickSettings,
                        modifier = Modifier.align(Alignment.TopStart)
                            .wrapContentWidth()
//                            .widthIn(max = 200.dp)

                    )
                }
            } else {
                Scaffold(
                    bottomBar = {
                        HorizontalNavigationBar(
                            navigationItems = navigationItems,
                            selected = selected,
                            quickSettings = { quickSettings(true) })
                    },
                    contentWindowInsets = WindowInsets.navigationBars
                        .add(WindowInsets.ime)
                        .add(WindowInsets.waterfall)
                        .add(WindowInsets.navigationBars),
                    modifier = Modifier
                        .imePadding()
                ) { pv ->
                    Box(
                        modifier = Modifier.fillMaxSize()
                            .padding(pv)
                    ) {
                        content()
                    }
                }
            }

        }

    }
}

@Stable
class NavigationBarState(
    private val openedSize: Dp = 400.dp,
    private val closedSize: Dp = 60.dp,
    private val hoverDelay: Long = 500,
    private val scope: CoroutineScope = CoroutineScope(DispatcherProvider.Default)
) {
    private val _width = MutableStateFlow(closedSize)
    val width = _width.asStateFlow()

    private val _opened = MutableStateFlow(false)
    val opened = _opened.asStateFlow()

    private var job: Job? = null
    fun open() {
        _width.update { openedSize }
        _opened.update { true }
    }

    fun openWithDelay() {
        job = scope.launch {
            delay(hoverDelay)
            open()
        }
    }

    fun close() {
        job?.cancel()
        _width.update { closedSize }
        _opened.update { false }
    }

    fun changeState() {
        if (opened.value) {
            close()
        } else {
            open()
        }
    }

}

data class NavigationBarScope(
    val state: State<NavigationBarState>
)

@Composable
fun rememberNavigationBarState(): MutableState<NavigationBarState> {
    val scope = rememberCoroutineScope()
    return remember { mutableStateOf(NavigationBarState(scope = scope)) }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun VerticalNavigationBar(
    navigationItems: List<NavigationItem>,
    modifier: Modifier = Modifier,
    selected: NavigationItem,
    quickSettings: @Composable (opened: Boolean) -> Unit = {},
    state: MutableState<NavigationBarState> = rememberNavigationBarState(),
) {
    val barState by state
    val rawWidth by barState.width.collectAsState()
    val width by animateDpAsState(targetValue = rawWidth)
    val opened by barState.opened.collectAsState()
    Surface(
        modifier = Modifier.fillMaxHeight()
            .width(width)
            .then(modifier)
            .onPointerEvent(PointerEventType.Enter) {
                barState.openWithDelay()
            }
            .onPointerEvent(PointerEventType.Exit) {
                barState.close()
            }.debbugable(),
        shadowElevation = if (opened) width / 10 else 0.dp
    ) {
        Box(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(8.dp)
                    .debbugable(),
                verticalArrangement = Arrangement.Top
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth()
                        .debbugable(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NaviagtionIcon(
                        icon = if (opened) Icons.Default.Close else Icons.Default.Menu,
                        selected = opened,
                        onClick = {
                            barState.changeState()
                        }
                    )
                    Crossfade(
                        opened,
                        modifier = Modifier
//                            .padding(8.dp)
                            .debbugable()
                            .fillMaxWidth()
                            .weight(1F)
                    ) {
                        if (it) {
                            Text(
                                "Online Education",
                                style = MaterialTheme.typography.displaySmall,
                                maxLines = 1,
                                //                            overflow = TextOverflow.Clip,
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .debbugable()
                            )

                        }
                    }
                }

                with(barState) {
                    navigationItems
                        .filter { it.navigationGroup != NavigationGroup.accountGroup }
                        .groupBy(NavigationItem::navigationGroup)
                        .forEach { navigationGroup ->
                            Crossfade(
                                opened,
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .debbugable()
//                                    .animateContentSize()
                            ) {
                                if (it) {
                                    Text(
                                        navigationGroup.key.groupeName,
                                        style = MaterialTheme.typography.labelLarge,
                                        maxLines = 1,
                                        overflow = TextOverflow.Clip
                                    )
                                }
                            }
                            HorizontalDivider(Modifier.fillMaxWidth())
                            navigationGroup.value.forEach { navigationItem ->
                                NavigationItem(
                                    icon = navigationItem.icon,
                                    text = navigationItem.name,
                                    selected = navigationItem == selected,
                                    onClick = navigationItem.onClick
                                )
                            }
                        }
                }
            }

            Crossfade(
                opened,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(8.dp)
//                    .animateContentSize()
                    .debbugable()
            ) {
//                if (it) {
//                    Card {
//                        Column(
//                            modifier = Modifier.padding(8.dp)
//                        ) {
//                            navigationItems
//                                .filter { it.navigationGroup == NavigationGroup.accountGroup }
//                                .forEach { navigationItem ->
//                                    Text(
//                                        navigationItem.name,
//                                        style = MaterialTheme.typography.bodyMedium,
//                                        maxLines = 1,
//                                        overflow = TextOverflow.Ellipsis
//                                    )
//                                    HorizontalDivider(Modifier.fillMaxWidth())
//
//                                }
//                        }
//                    }
//                } else {
//                    val navItem = navigationItems
//                        .firstOrNull() { it.navigationGroup == NavigationGroup.accountGroup }
//                    if (navItem != null) {
//                        HorizontalNavigationIcon(
//                            icon = navItem.icon,
//                            text = navItem.name,
//                            selected = navItem == selected,
//                            onClick = navItem.onClick,
//                            opened = opened
//                        )
//                    }
//                }

                quickSettings(it)

            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun NavigationBarState.NavigationItem(
    icon: ImageVector,
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val navBarState = this
    val opened by navBarState.opened.collectAsState()

    HorizontalNavigationIcon(
        icon = icon,
        text = text,
        selected = selected,
        onClick = onClick,
        opened = opened
    )


}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NaviagtionIcon(
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
) {
    var hovered by remember { mutableStateOf(false) }

    val containerColor by animateColorAsState(
        if (selected) {
            if (hovered) {
                MaterialTheme.colorScheme.tertiary
            } else {
                MaterialTheme.colorScheme.tertiaryContainer
            }

        } else {
            if (hovered) {
                MaterialTheme.colorScheme.surfaceVariant
            } else {
                MaterialTheme.colorScheme.background
            }

        }
    )

    Card(
        modifier = Modifier
//            .wrapContentSize()
            .debbugable()
            .onPointerEvent(PointerEventType.Enter) {
                hovered = true
            }
            .onPointerEvent(PointerEventType.Exit) {
                hovered = false
            },
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        onClick = onClick
    ) {
        Crossfade(icon) {
            Icon(
                imageVector = it, null,
                modifier = Modifier
                    .padding(8.dp)
                    .debbugable()
            )
        }
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HorizontalNavigationIcon(
    icon: ImageVector,
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    opened: Boolean,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    var hovered by remember { mutableStateOf(false) }

    val containerColor by animateColorAsState(
        if (selected) {
            if (hovered) {
                MaterialTheme.colorScheme.tertiary
            } else {
                MaterialTheme.colorScheme.tertiaryContainer
            }

        } else {
            if (hovered) {
                MaterialTheme.colorScheme.surfaceVariant
            } else {
                MaterialTheme.colorScheme.background
            }

        }


    )

    Card(
        modifier = modifier
            .debbugable()
            .onPointerEvent(PointerEventType.Enter) {
                hovered = true
            }
            .onPointerEvent(PointerEventType.Exit) {
                hovered = false
            },
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(8.dp)
                .debbugable(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon, null,
                modifier = Modifier.debbugable()
            )
            Crossfade(
                opened,
                modifier = Modifier.debbugable()
            ) {
                if (it) {
                    Text(
                        text = text,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(start = 8.dp)
                            .debbugable()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HorizontalNavigationBar(
    navigationItems: List<NavigationItem>,
    selected: NavigationItem,
    quickSettings: @Composable () -> Unit
) {

    BottomAppBar(
        modifier = Modifier.debbugable()
    ) {
        navigationItems
            .filter { it.navigationGroup == NavigationGroup.mainGroup }
            .forEach { navigationItem ->
                key(navigationItem) {
                    HorizontalNavigationBarItem(navigationItem, selected = navigationItem == selected)
                }
            }
        var menuOpened by remember { mutableStateOf(false) }
        key("collapsableMenu") {
            HorizontalNavigationBarItem(
                navigationItem = NavigationItem(
                    name = "Меню",
                    icon = Icons.Default.Menu,
                    onClick = {
                        menuOpened = !menuOpened
                    }
                ),
                selected = menuOpened
            )
        }

        if (menuOpened) {
            ModalBottomSheet(
                onDismissRequest = {
                    menuOpened = false
                },
//                windowInsets = WindowInsets(bottom = 56.dp)
//                    .add(WindowInsets.navigationBars),
                modifier = Modifier.debbugable()
            ) {
                Column(
                    Modifier.fillMaxSize()
                        .padding(horizontal = 8.dp)
                        .debbugable()
                ) {
                    Box(
                        Modifier.fillMaxWidth()
                            .padding(8.dp)
                            .debbugable()
                    ) {
                        quickSettings()
                    }
                    navigationItems.filterNot { it.navigationGroup == NavigationGroup.mainGroup }
                        .groupBy { it.navigationGroup }
                        .forEach { navigationGroup ->
                            Text(
                                navigationGroup.key.groupeName,
                                style = MaterialTheme.typography.labelMedium
                            )
                            HorizontalDivider(modifier = Modifier.fillMaxWidth())
                            navigationGroup.value.forEach { navigationItem ->
                                HorizontalNavigationIcon(
                                    icon = navigationItem.icon,
                                    text = navigationItem.name,
                                    selected = navigationItem == selected,
                                    onClick = navigationItem.onClick,
                                    opened = true
                                )
                            }
                        }
                }
            }
        }
    }
}

@Composable
fun RowScope.HorizontalNavigationBarItem(
    navigationItem: NavigationItem,
    selected: Boolean,
) {
    with(navigationItem) {
        NavigationBarItem(
            onClick = onClick,
            icon = {
                Icon(icon, null)
            },
            label = {
                Text(name)
            },
            selected = selected,
            modifier = Modifier.debbugable()
        )
    }
}

fun Modifier.onPointerEvent(
    pointerEventType: PointerEventType,
    onPointerEvent: () -> Unit
) = pointerInput(pointerEventType) {


    awaitEachGesture {
        if(awaitPointerEvent(PointerEventPass.Initial).type == pointerEventType) {
            onPointerEvent()
        }
    }
}