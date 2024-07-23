package ui.pages

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.ArticleSimpleModel

@Composable
fun MainArticleScreen(
    articleDataList: List<ArticleSimpleModel>,
    updateArticleList: () -> Unit,
) {

    println("reload MainArticleScreen")


    LazyColumn(
        modifier = Modifier.height(10.dp),
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
    Surface(
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 1.dp,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(15.dp).animateContentSize()
            .clickable { println("显示文章详情") },
    ) {

        Column(
            modifier = Modifier.padding(15.dp)
        ) {
            Text(
                text = item.articleTitle ?: "",
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                modifier = Modifier.padding(top = 15.dp),
                text = item.articleBrief?.replace(Regex("[\t\n ]"), "") ?: "",
                maxLines = 3,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }

}
