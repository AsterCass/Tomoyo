package ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.ArticleSimpleModel
import data.model.ArticleScreenModel
import data.model.MainScreenModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import theme.subTextColor
import tomoyo.composeapp.generated.resources.Res
import tomoyo.composeapp.generated.resources.search_keyword
import ui.components.MainBaseCardBox
import ui.views.ArticleDetailScreen


object MainArticlesScreen : Screen {

    private fun readResolve(): Any = MainArticlesScreen

    @Composable
    override fun Content() {
        MainArticlesScreen()
    }

}

@Composable
fun MainArticlesScreen(
    screenModel: ArticleScreenModel = koinInject(),
    mainModel: MainScreenModel = koinInject(),
) {
    //navigation
    val navigator = LocalNavigator.currentOrThrow

    //soft keyboard
    val keyboardController = LocalSoftwareKeyboardController.current

    //coroutine
    val articleApiCoroutine = rememberCoroutineScope()

    //data
    val articleDataList = screenModel.articleDataList.collectAsState().value
    val constraints = mainModel.mainPageContainerConstraints.collectAsState().value
    val articleIsLoadAll = screenModel.articleIsLoadAll.collectAsState().value
    val articleDataKey = screenModel.articleDataKey.collectAsState().value

    val density = LocalDensity.current
    val minHeightDp = with(density) { constraints.minHeight.toDp() }

    var searchArticleKey by remember { mutableStateOf(articleDataKey) }


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
                    OutlinedTextField(
                        modifier = Modifier.height(50.dp),
                        value = searchArticleKey,
                        onValueChange = { searchArticleKey = it },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent,
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = null,
                            )
                        },
                        textStyle = MaterialTheme.typography.bodyMedium,
                        placeholder = {
                            Text(
                                text = stringResource(Res.string.search_keyword),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.subTextColor
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                screenModel.clearResetKeyword(searchArticleKey)
                                searchArticleKey = ""
                                keyboardController?.hide()
                            },
                            onDone = {
                                screenModel.clearResetKeyword(searchArticleKey)
                                searchArticleKey = ""
                                keyboardController?.hide()
                            }
                        ),
                        maxLines = 1,
                    )

                }

            }

        }

        items(articleDataList.size) { index ->
            ArticleListItem(item = articleDataList[index],
                toDetail = {
                    screenModel.updateReadingMeta(it)
                    navigator.push(ArticleDetailScreen())
                })
        }

        item {
            if (!articleIsLoadAll) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            articleApiCoroutine.launch {
                delay(1000)
                screenModel.updateArticleList()
            }
        }

    }

}


@Composable
fun ArticleListItem(
    item: ArticleSimpleModel,
    toDetail: (ArticleSimpleModel) -> Unit,
) {

    MainBaseCardBox(
        modifier = Modifier
            .wrapContentHeight()
            .padding(start = 25.dp, end = 25.dp)
            .padding(5.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable { toDetail(item) }
                .padding(15.dp)
        ) {
            //保证子组件高度相同
            Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                Column(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceEvenly,
                ) {
                    Text(
                        text = item.articleTitle ?: "",
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = item.authorName ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.subTextColor
                        )
                        Text(
                            text = item.createTime ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.subTextColor
                        )
                    }
                }

            }


            Box(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .clip(
                        shape = RoundedCornerShape(15.dp)
                    )
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = item.articleBrief?.replace(Regex("[\t\n ]"), "") ?: "",
                    maxLines = 5,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

        }
    }


}
