package ru.online.education.app.feature.course.presentation.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.online.education.app.core.util.model.ApiState
import ru.online.education.app.feature.course.domain.model.mapper.toCourse
import ru.online.education.app.feature.course.presentation.viewModel.AllCoursesScreenViewModel

@Composable
fun CoursesScreen(allCoursesScreenViewModel: AllCoursesScreenViewModel) {
    val courses by allCoursesScreenViewModel.courseResult.collectAsState()
    AnimatedContent(courses) { apiState ->
        when (apiState) {
            is ApiState.Default -> Unit
            is ApiState.Error -> {
                Text(apiState.error.message ?: "Error")
            }

            is ApiState.Loading -> {
                CircularProgressIndicator()
            }

            is ApiState.Success -> {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(150.dp)
                ) {
                    apiState.data.values.forEach { courseDto ->
                        item {
                            CourseSmallCard(
                                courseDto.toCourse(),
                                onClick = {

                                }
                            )
                        }
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