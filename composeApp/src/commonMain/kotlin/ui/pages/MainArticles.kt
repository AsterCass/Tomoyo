package ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import data.ArticleSimpleModel

@Composable
fun MainArticleScreen(
    articleDataList: List<ArticleSimpleModel>?,
    modifier: Modifier = Modifier,
    updateArticleList: @Composable (Boolean) -> Unit,
) {
    updateArticleList(false)
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = articleDataList.toString(),
            style = MaterialTheme.typography.headlineSmall
        )
    }

}