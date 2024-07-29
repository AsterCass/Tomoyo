package ui.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import data.ArticleSimpleModel
import data.model.ArticleScreenModel
import data.model.MainScreenModel
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import ui.components.MainBaseCardBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainArticleScreen(
    screenModel: ArticleScreenModel = koinInject(),
    mainModel: MainScreenModel = koinInject(),
) {

    //coroutine
    val articleApiCoroutine = rememberCoroutineScope()

    //data
    val articleDataList = screenModel.articleDataList.collectAsState().value
    val constraints = mainModel.mainPageContainerConstraints.value

    val density = LocalDensity.current
    val minHeightDp = with(density) { constraints.minHeight.toDp() }

    var searchArticleKey by remember { mutableStateOf("") }


    //data
    LazyColumn(
        modifier = Modifier.height(minHeightDp),
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {

        item {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                MainBaseCardBox(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(top = 15.dp, start = 75.dp, end = 75.dp, bottom = 25.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = null,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                        BasicTextField(
                            modifier = Modifier.height(40.dp)
                                .padding(vertical = 10.dp, horizontal = 5.dp),
                            value = searchArticleKey,
                            onValueChange = { searchArticleKey = it },
                            textStyle = MaterialTheme.typography.bodyMedium
                        )
                    }


                }

            }

        }

        items(articleDataList.size) { index ->
            ArticleListItem(item = articleDataList[index])
        }
        item {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(16.dp)
                )
            }

            articleApiCoroutine.launch {
                screenModel.updateArticleList()
            }

        }
    }

}


@Composable
fun ArticleListItem(item: ArticleSimpleModel) {
    MainBaseCardBox(
        modifier = Modifier
            .wrapContentHeight()
            .padding(start = 25.dp, end = 25.dp)
            .padding(5.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable { println("显示文章详情") }
                .padding(15.dp)
        ) {
            Text(
                text = item.articleTitle ?: "",
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                modifier = Modifier.padding(top = 15.dp),
                text = item.articleBrief?.replace(Regex("[\t\n ]"), "") ?: "",
                maxLines = 5,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }


}
