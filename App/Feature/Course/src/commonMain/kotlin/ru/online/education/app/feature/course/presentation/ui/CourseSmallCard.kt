package ru.online.education.app.feature.course.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.rememberImagePainter
import ru.online.education.domain.repository.model.CourseDto
import ru.online.education.domain.repository.model.Image
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.online.education.app.core.util.compose.*
import ru.online.education.app.feature.course.domain.model.Course


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseSmallCard(
    course: Course,
    onClick: (Course) -> Unit,
    modifier: Modifier = Modifier,

    ) {
    val colorScheme = MaterialTheme.colorScheme
    val background /*by remember(course.background) { mutableStateOf(course.background) }*/ =
        Image.defaultColor as Image
    val textColor by remember {
        derivedStateOf {
            getReadableColorVariant(
                (background as? Image.Color)?.toComposeColor() ?: colorScheme.onBackground
            )
        }
    }
    Card(
        onClick = {
            onClick(course)
        },
        modifier = modifier
            .padding(5.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (background is Image.Color) {
                background.toComposeColor()
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            },
            contentColor =  if (background is Image.Color){
                contentColorFor(background.toComposeColor())
            }else{
                MaterialTheme.colorScheme.onSurfaceVariant
            }

        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp,
            pressedElevation = 0.dp,
            focusedElevation = 5.dp
        )
    ) {

//        Box(
//            modifier = Modifier.fillMaxSize()
//                .background(Color.Transparent)
//        ) {
//
//           /* Box(modifier = Modifier.fillMaxSize()) {
//                when (background) {
//                    is Image.Color -> Box(
//                        Modifier.fillMaxSize()
//                            .background(color = ((background as? Image.Color) ?: Image.Color(1, 1, 1)).toComposeColor())
//                    )
//
//                    is Image.ImageResource -> {
//                        val url = (background as Image.ImageResource).src
//
//                        if (url.isNotEmpty()) {
//                            val painter = rememberImagePainter(
//                                url = url,
//                            )
//
//                            Image(painter, null)
//                        } else {
//                            Modifier.fillMaxSize()
//                                .background(color = Image.defaultColor.toComposeColor())
//                        }
//                    }
//
//                    Image.NoImage -> Unit
//                }
//            }*/
//
//
//        }

        Column(

            Modifier.padding(8.dp)
//                .align(Alignment.BottomStart)
//                .background(Color.Transparent)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.Start
        ) {

            Text(
                text = course.name,
//                color = textColor,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                textAlign = TextAlign.Start,
                softWrap = true
            )
            Text(
                text = course.courseCreator,
                color = textColor,
                fontWeight = FontWeight.Light,
                style = MaterialTheme.typography.titleSmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                textAlign = TextAlign.Start,
                softWrap = true
            )
            Text(
                text = course.courseCategory,
                color = textColor,
                fontWeight = FontWeight.ExtraLight,
                style = MaterialTheme.typography.labelMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                textAlign = TextAlign.Start,
                softWrap = true
            )
        }
    }
}

@Preview
@Composable
fun CourseCardPreview() {
    CourseSmallCard(
        Course(
            name = "CourseName",
            shortDescription = "shortDescription",
            longDescription = "longDescription",
            courseCategory = "Category",
            courseCreator = "Created by"
        ),
        onClick = {

        }
    )
}

