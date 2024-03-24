package com.arkivanov.sample.shared.multipane

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.Child
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.sample.shared.multipane.details.CourseDetailsComponent
import destination.course.list.CourseListComponent
import navgation.destination.root.course.details.CourseDetailsContent
import navgation.destination.root.course.list.CourseListComponent
import ru.online.education.app.core.util.compose.LocalDeviceConfiguration
import ru.online.education.app.core.util.compose.debbugable

@Composable
internal fun CoursesContent(component: MultiPaneComponent, modifier: Modifier = Modifier) {
    val children by component.children.subscribeAsState()
    val listChild = children.listChild
    val detailsChild = children.detailsChild

    val saveableStateHolder = rememberSaveableStateHolder()

    val listPane: @Composable (Child.Created<*, CourseListComponent>) -> Unit =
        remember {
            movableContentOf { (config, context) ->
                saveableStateHolder.SaveableStateProvider(key = config.hashCode()) {
                    CourseListComponent(
                        context = context,
                    )
                }
            }
        }

    val detailsPane: @Composable (Child.Created<*, CourseDetailsComponent>) -> Unit =
        remember {
            movableContentOf { (config, component) ->
                saveableStateHolder.SaveableStateProvider(key = config.hashCode()) {
                    CourseDetailsContent(
                        context = component,
                    )
                }
            }
        }

    saveableStateHolder.OldDetailsKeyRemoved(selectedDetailsKey = children.detailsChild?.configuration?.hashCode())

    BoxWithConstraints(modifier = modifier) {
        when {
            children.isMultiPane ->
                Row(
                    modifier = Modifier.fillMaxSize()
                        .debbugable()
                ) {
                    Box(
                        modifier = Modifier.fillMaxHeight()
                            .weight(0.4F)
                            .debbugable()
                    ) {
                        listPane(children.listChild)
                    }

                    Box(
                        modifier = Modifier.fillMaxHeight()
                            .weight(0.6F)
                            .debbugable()
                    ) {
                        AnimatedContent(children.detailsChild){ detailsChild ->
                            if (detailsChild != null) {
                                detailsPane(detailsChild)
                            }
                        }

                    }
                }

            detailsChild != null -> detailsPane(detailsChild)
            else -> listPane(listChild)
        }

        val localDeviceConfiguration = LocalDeviceConfiguration.current

        LaunchedEffect(localDeviceConfiguration) {
            component.setMultiPane(localDeviceConfiguration.isHorizontal)
        }
    }
}

@Composable
private fun SaveableStateHolder.OldDetailsKeyRemoved(selectedDetailsKey: Any?) {
    var lastDetailsKey by remember { mutableStateOf(selectedDetailsKey) }

    if (selectedDetailsKey == lastDetailsKey) {
        return
    }

    val keyToRemove = lastDetailsKey
    lastDetailsKey = selectedDetailsKey

    if (keyToRemove == null) {
        return
    }

    DisposableEffect(keyToRemove) {
        removeState(key = keyToRemove)
        onDispose {}
    }
}
