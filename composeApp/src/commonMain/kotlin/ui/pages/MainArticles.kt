package ui.pages

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.ArticleSimpleModel
import data.model.ArticleScreenModel
import org.koin.compose.koinInject

@Composable
fun MainArticleScreen(
//    constraints: Constraints,
//    articleDataList: List<ArticleSimpleModel>,
//    updateArticleList: () -> Unit,
//    state: Boolean,

    screenModel: ArticleScreenModel = koinInject(),
) {


//    val state = screenModel.openBottomSheet.value
    val state = screenModel.openBottomSheet.collectAsState().value

    OutlinedButton(
        onClick = { screenModel.openBottomSheet(!state) },
    ) {
        Text("测试")
    }


    println("reload MainArticleScreen ")

    Column {
        Text("这是文章页面" + state)
    }

//    val density = LocalDensity.current
//    val minHeightDp = with(density) { constraints.minHeight.toDp() }
//    println(minHeightDp)
//
//    LazyColumn(
//        modifier = Modifier.height(minHeightDp)
//    ) {
//        items(articleDataList.size) { index ->
//            ArticleListItem(item = articleDataList[index])
//        }
//        item {
//            CircularProgressIndicator(
//                modifier = Modifier.fillMaxWidth().padding(16.dp)
//            )
//            updateArticleList()
//        }
//    }


}


@Composable
fun ArticleListItem(item: ArticleSimpleModel) {
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
