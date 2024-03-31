package presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paging.LoadStateLoading
import app.cash.paging.compose.collectAsLazyPagingItems
import domain.PublicationScreenState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import model.Publication
import ru.online.education.app.core.util.compose.debbugable
import kotlin.time.Duration.Companion.milliseconds

@OptIn(FlowPreview::class, ExperimentalFoundationApi::class)
@Composable
fun PublicationListScreen(
    state: PublicationScreenState,
    editable: Boolean = false,
    onEditClick : (Publication) -> Unit = {}
) {
    val publications = state.publications
        .collectAsLazyPagingItems()
    val isLoading =
        publications.loadState.append is LoadStateLoading || publications.loadState.refresh is LoadStateLoading
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .debbugable()
    ) {
        items(publications.itemCount) { index: Int ->
            val publication = publications[index]
            if (publication != null) {
                key(publication.id) {
                    PublicationCard(
                        publication = publication,
                        collapsableContent = {

                        },
                        modifier = Modifier.fillMaxWidth()
                            .animateItemPlacement(),
                        editable = editable,
                        onEditClick = {
                            onEditClick(publication)
                        }
                    )
                }
                key("spacer") {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
        item {
            if (isLoading) {
                Box(modifier = Modifier.fillParentMaxWidth().debbugable()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center).debbugable())
                }
            }
        }
    }
}