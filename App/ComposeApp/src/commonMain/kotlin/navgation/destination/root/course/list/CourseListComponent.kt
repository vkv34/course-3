package navgation.destination.root.course.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import destination.course.list.CourseListComponent
import ru.online.education.app.core.util.compose.debbugable
import ru.online.education.app.feature.course.presentation.ui.CoursesList

@Composable
fun CourseListComponent(
    context: CourseListComponent
) {
    CoursesList(
        modifier = Modifier
            .fillMaxSize()
            .debbugable(),
        onCourseClick = { course -> context.onCourseClicked(course.id.toLong()) },
        allCoursesScreenViewModel = context.allCoursesViewModel
    )
}