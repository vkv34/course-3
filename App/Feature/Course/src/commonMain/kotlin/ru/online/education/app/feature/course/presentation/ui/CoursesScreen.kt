package ru.online.education.app.feature.course.presentation.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.collectAsLazyPagingItems
import org.jetbrains.compose.ui.tooling.preview.Preview
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
    val refreshState = courses.loadState.refresh
    val appendState = courses.loadState.append
    LazyVerticalGrid(
        columns = GridCells.Adaptive(400.dp),
        modifier = modifier,

    ) {
        items(courses.itemCount) { index: Int ->
            val course = courses[index]
            if (course != null) {
                key(course.id) {
                    CourseSmallCard(
                        course = course,
                        onClick = onCourseClick,
                        modifier = Modifier.fillMaxSize()
                            .debbugable()
                            .animateItemPlacement()
                    )
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