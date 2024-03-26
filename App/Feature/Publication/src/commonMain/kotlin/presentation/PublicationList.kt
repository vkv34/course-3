package presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paging.LoadStateLoading
import app.cash.paging.compose.collectAsLazyPagingItems
import domain.PublicationScreenState
import ru.online.education.app.core.util.compose.debbugable

@Composable
fun PublicationListScreen(
    state: PublicationScreenState
) {
    val publications = state.publications.collectAsLazyPagingItems()
    val isLoading =
        publications.loadState.append is LoadStateLoading || publications.loadState.refresh is LoadStateLoading
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .debbugable()
    ) {
        items(publications.itemCount) { index: Int ->
            val publication = publications[index]
            if (publication != null) {
                PublicationCard(
                    publication = publication,
                    collapsableContent = {

                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(8.dp))
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