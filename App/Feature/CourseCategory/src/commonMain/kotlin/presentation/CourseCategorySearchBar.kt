package presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.window.PopupProperties
import app.cash.paging.LoadStateLoading
import app.cash.paging.compose.collectAsLazyPagingItems
import presentation.viewModel.CourseCategorySearchBarState
import ru.online.education.app.core.util.compose.debbugable

@Composable
fun CourseCategorySearchBar(
    state: CourseCategorySearchBarState,
    modifier: Modifier = Modifier,
    canAdd: Boolean = false,
    onAddClick: () -> Unit = {},
) {
    val query by state.query.collectAsState()
    val courseCategories = state.courseCategorySearchPager.collectAsLazyPagingItems()
    var expanded by remember { mutableStateOf(false) }
    val isLoading =
        courseCategories.loadState.refresh is LoadStateLoading || courseCategories.loadState.append is LoadStateLoading
    BoxWithConstraints(
        modifier = modifier.debbugable()
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = {
                expanded = true
                state.search(it)
            },
            label = {
                Text("Категория курса")
            },
            modifier = Modifier.debbugable()
                .fillMaxWidth()
//                .clickable(
//                    role = Role.DropdownList
//                ) {
//                    expanded = true
//                }
            ,
            trailingIcon = {
                IconButton(
                    onClick = {
                        expanded = !expanded
                    }

                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }
            },
            placeholder = {
                Text("Начните вводить...")
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(this.maxWidth)
                .debbugable()
                .animateContentSize(),
            properties = PopupProperties(
                dismissOnClickOutside = true,
                focusable = false
            )
        ) {
            AnimatedVisibility(
                isLoading,
                modifier = Modifier.debbugable()
            ) {
                DropdownMenuItem(
                    leadingIcon = {
                        CircularProgressIndicator()
                    },
                    text = {
                        Text("Загрузка...")
                    },
                    enabled = false,
                    onClick = {},
                    modifier = Modifier.debbugable(),
                )
            }
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            if (courseCategories.itemCount == 0 && canAdd) {
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    },
                    text = {
                        Text("Добавить категорию")
                    },
                    enabled = true,
                    onClick = { onAddClick() },

                    modifier = Modifier.debbugable(),
                )
                HorizontalDivider(modifier = Modifier.fillMaxWidth())
            }
            repeat(courseCategories.itemCount) { index ->
                val category = courseCategories[index]
                if (category != null) {
                    key(category.id.hashCode()) {
                        DropdownMenuItem(
                            text = {
                                Text(category.name)
                            },
                            onClick = {
                                expanded = false
                                state.selectCategory(category)
                            },
                            modifier = Modifier.debbugable(),
                        )
                    }
                    HorizontalDivider(modifier = Modifier.fillMaxWidth())
                }
            }

        }
    }

}