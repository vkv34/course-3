package ru.online.education.app.feature.course.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.rememberImagePainter
import model.CourseDto
import model.Image
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.online.education.app.core.util.compose.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseSmallCard(
    course: CourseDto,
    onClick: (CourseDto) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = {
            onClick(course)
        },
        modifier = modifier
    ) {

        Box {

            Box(modifier = Modifier.fillMaxSize()) {
                val background by remember(course.background) { mutableStateOf(course.background) }
                when (background) {
                    is Image.Color -> Box(
                        Modifier.fillMaxSize()
                            .background(color = (background as Image.Color).toComposeColor())
                    )

                    is Image.ImageResource -> {
                        val painter = rememberImagePainter(
                            url = (background as Image.ImageResource).src,
                        )

                        Image(painter, null)

                    }

                    Image.NoImage -> Unit
                }
            }

            Column(

                Modifier.padding(8.dp)
                    .align(Alignment.BottomStart)
            ) {


                Text(
                    course.name,
                    color = getReadableColorVariant((course.background as Image.Color).toComposeColor()),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    textAlign = TextAlign.Start,
                    softWrap = true
                )
            }


        }
    }
}

