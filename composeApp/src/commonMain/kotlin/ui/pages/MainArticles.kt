package ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.ArticleSimpleModel

@Composable
fun MainArticleScreen(
    articleDataList: List<ArticleSimpleModel>,
    modifier: Modifier = Modifier,
    updateArticleList: @Composable () -> Unit,
) {
    LazyColumn(
        modifier = modifier.height(10.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        items(articleDataList.size) { index ->
            ListItem(item = articleDataList[index])
        }
        item {
            CircularProgressIndicator(
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )
            updateArticleList()
        }
    }


}


@Composable
fun ListItem(item: ArticleSimpleModel) {
    Text(
        text = item.toString(), style = MaterialTheme.typography.headlineSmall
    )
}
