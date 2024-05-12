package ru.online.education.app.feature.course.presentation.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Unarchive
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paging.LoadStateLoading
import app.cash.paging.compose.collectAsLazyPagingItems
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.online.education.app.core.util.compose.ConfirmPopup
import ru.online.education.app.core.util.compose.debbugable
import ru.online.education.app.feature.course.domain.model.Course
import ru.online.education.app.feature.course.presentation.viewModel.AllCoursesScreenViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CoursesList(
    modifier: Modifier = Modifier,
    onCourseClick: (Course) -> Unit,
    allCoursesScreenViewModel: AllCoursesScreenViewModel
) {
    val courses = allCoursesScreenViewModel.courses.collectAsLazyPagingItems()
    val archivedCourses = allCoursesScreenViewModel.archivedCourses.collectAsLazyPagingItems()
    val refreshState = courses.loadState.refresh
    val appendState = courses.loadState.append
    val isLoading = refreshState is LoadStateLoading || appendState is LoadStateLoading
    val tab by allCoursesScreenViewModel.tab.collectAsState()
    val screenState by allCoursesScreenViewModel.screenState.collectAsState()


    Column(
        modifier
    ) {

        TabRow(
            selectedTabIndex = tab,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = tab == 0,
                onClick = {
                    allCoursesScreenViewModel.tab.value = 0
                },
                text = {
                    Text("Курсы")
                }
            )
            Tab(
                selected = tab == 1,
                onClick = {
                    allCoursesScreenViewModel.tab.value = 1
                },
                text = {
                    Text("Архив курсов")
                }
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Adaptive(400.dp),
            modifier = Modifier.fillMaxSize(),

            ) {
            items(
                if (tab == 0)
                    courses.itemCount
                else
                    archivedCourses.itemCount
            ) { index: Int ->
                val course = if (tab == 0)
                    courses[index]
                else
                    archivedCourses[index]

                if (course != null) {
                    key(course.id) {
                        Box(
                            modifier = Modifier.fillMaxSize()
                                .debbugable()
                                .animateItemPlacement()
                        ) {
                            CourseSmallCard(
                                course = course,
                                onClick = onCourseClick,
                                modifier = Modifier.fillMaxSize()
                                    .debbugable()
                            )

                            if (screenState.canEdit) {
                                var opened by remember { mutableStateOf(false) }
                                IconButton(
                                    onClick = {
                                        opened = true
                                    },
                                    modifier = Modifier.align(Alignment.CenterEnd)
                                        .padding(8.dp)
                                ) {
                                    Icon(
                                        imageVector = if (tab == 0) Icons.Default.Archive else Icons.Default.Unarchive,
                                        null
                                    )

                                    if (opened) {
                                        ConfirmPopup(
                                            title = if (tab == 0) "Переместить в архив?" else "Убрать из архива?",
                                            subTitle = "Подтвердите действие",
                                            onConfirm = {
                                                allCoursesScreenViewModel.archive(courseId = course.id)
                                            },
                                            onDismiss = {
                                                opened = false
                                            }
                                        )
                                    }
                                }


                            }
                        }
                    }

                }
            }



            if (isLoading) {
                item(key = "loading") {
                    Box(Modifier.fillMaxWidth()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }

            }
        }
    }
}

@Preview
@Composable
fun A() {
    Text("sdaasd")
}