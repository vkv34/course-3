package preview

import presentation.CourseSmallCard
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import model.Course
import model.Image
import model.User

@androidx.compose.desktop.ui.tooling.preview.Preview
@androidx.compose.runtime.Composable
fun CourseCardPreview() {
    CourseSmallCard(
        Course(
            name = "Курс по программированию",
            creator = User(firstName = "Имя", secondName = "Фамилия", lastName = "Отчество"),
            description = "Курс по программированию",
            background = Image.Color(100,150, 100 , 150)
        ),
        onClick = {},
        modifier = Modifier.size(180.dp)
    );
}