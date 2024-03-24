package ru.online.education.app.feature.course.presentation.ui

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.collectAsLazyPagingItems
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.online.education.app.feature.course.domain.model.Course
import ru.online.education.app.feature.course.presentation.viewModel.AllCoursesScreenViewModel

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
        columns = GridCells.Adaptive(200.dp),
        modifier = modifier,

    ) {
        items(courses.itemCount) { index: Int ->
            val course = courses[index]
            if (course != null) {
                CourseSmallCard(
                    course = course,
                    onClick = onCourseClick
                )
            }
        }
    }
}

@Preview
@Composable
fun A() {
    Text("sdaasd")
}