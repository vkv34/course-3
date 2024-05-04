package ru.online.education.app.feature.publication.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.cash.paging.LoadStateLoading
import app.cash.paging.compose.collectAsLazyPagingItems
import ru.online.education.app.core.util.compose.ConfirmIconButton
import ru.online.education.app.core.util.compose.debbugable
import ru.online.education.app.feature.publication.domain.PublicationScreenState
import ru.online.education.domain.repository.model.UserOnCourseDto
import ru.online.education.domain.repository.model.UserRole

internal val availableRoles = arrayOf(
    UserRole.Student,
    UserRole.Admin
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserOnCourseCard(
    userOnCourseDto: UserOnCourseDto,
    modifier: Modifier = Modifier,
    editable: Boolean = false,
    onRoleChange: (UserRole) -> Unit = {},
    onDelete: () -> Unit = {}
) {
    Card(
        modifier = modifier,
    ) {
        ListItem(
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent
            ),
            headlineContent = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        userOnCourseDto.userDto.fio,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight(500)
                    )
                    if (editable) {
                        Spacer(Modifier.width(16.dp))
                        SingleChoiceSegmentedButtonRow(
                        ) {
                            availableRoles.forEach { role ->
                                SegmentedButton(
                                    selected = userOnCourseDto.role == role,
                                    onClick = {
                                        onRoleChange(role)
                                    },
                                    shape = MaterialTheme.shapes.small,
                                    label = {
                                        Text(role.toString())
                                    }
                                )
                                Spacer(Modifier.width(16.dp))
                            }
                        }
                        Spacer(Modifier.width(16.dp))
                        ConfirmIconButton(
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    null
                                )
                            },
                            title = "Исключить слушателя?",
                            onConfirm = {
                                onDelete()
                            }
                        )
                    } else {
                        Text(
                            text = userOnCourseDto.role.toString(),
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UsersList(
    screenState: PublicationScreenState,
    editable: Boolean
) {
    val usersPagingData = screenState.usersOnCourse.collectAsLazyPagingItems()
    val isLoading =
        usersPagingData.loadState.append is LoadStateLoading || usersPagingData.loadState.refresh is LoadStateLoading
    var firstUserIndex by remember { mutableStateOf<Int?>(null) }


    LaunchedEffect(isLoading){
        if (isLoading){
            firstUserIndex = null
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = 32.dp)
        ) {
            stickyHeader {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Преподаватели")
                    HorizontalDivider(modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(8.dp))

                }
            }
            repeat(usersPagingData.itemCount) { index ->
                val userOnCourse = usersPagingData[index]
                if (index == firstUserIndex) {
                    stickyHeader {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Слушатели")
                            HorizontalDivider(modifier = Modifier.fillMaxWidth())
                            Spacer(Modifier.height(8.dp))
                        }
                    }
                }
                if (userOnCourse != null) {
                    if (userOnCourse.role == UserRole.Student && firstUserIndex == null) {
                        firstUserIndex = index
                    }
                    item(
                        key = userOnCourse.id
                    ) {
                        UserOnCourseCard(
                            userOnCourse,
                            modifier = Modifier.fillMaxWidth()
                                .animateItemPlacement(),
                            editable = editable,
                            onRoleChange = { screenState.changeUserOnCourseRole(userOnCourse, newRole = it) },
                            onDelete = { screenState.deleteUserFromCourse(userOnCourse.id) }
                        )
                    }
                    item(
                    ) {
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }

            item {
                if (isLoading) {
                    Box(modifier = Modifier.fillParentMaxWidth().debbugable()) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                                .padding(18.dp)
                                .debbugable()
                        )
                    }
                }
            }
        }

        IconButton(
            onClick = screenState::reloadUsersOnCourse,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        ) {
            Icon(imageVector = Icons.Default.Refresh, null)
        }
    }
}


