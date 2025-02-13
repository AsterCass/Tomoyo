package ui.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.LocalPlatformContext
import com.github.panpf.sketch.request.ImageRequest
import constant.enums.ViewEnum
import data.model.ArticleScreenModel
import data.model.MainScreenModel
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import theme.halfTransSurfaceVariant
import theme.subTextColor


class ArticleDetailScreen : Screen {

    override val key: ScreenKey = "${ViewEnum.ARTICLE_DETAIL.code}$uniqueScreenKey"

    @Composable
    override fun Content() {
        //inject
        val mainModel: MainScreenModel = koinInject()
        val articleScreenModel: ArticleScreenModel = koinInject()
        val configBlock: (ImageRequest.Builder.() -> Unit) = koinInject()

        //context for image
        val localPlatformContext = LocalPlatformContext.current

        //navigation
        val navigator = LocalNavigator.currentOrThrow
        val articleContent = articleScreenModel.readingArticleData.collectAsState().value
        val token = mainModel.userState.value.token

        //coroutine
        val articleApiCoroutine = rememberCoroutineScope()

        //data
        val articleData = articleScreenModel.readingArticleMeta.value


        if (token.isBlank()) {
            navigator.push(UserLoginScreen())
            return
        }

        articleApiCoroutine.launch {
            articleScreenModel.updateReadingArticleData(articleData.id ?: "", token)
        }

        Column(
            Modifier.fillMaxSize()
//                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(vertical = 4.dp, horizontal = 20.dp),
        ) {
            Box(
                Modifier.weight(0.07f),
                contentAlignment = Alignment.CenterStart
            ) {
                Button(
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.halfTransSurfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                    contentPadding = PaddingValues(0.dp),
                    onClick = { navigator.pop() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = null,
                    )
                }
            }
            Column(
                modifier = Modifier
                    .weight(0.93f)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column {
                    Text(
                        text = articleData.articleTitle ?: "",
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                            .height(IntrinsicSize.Min),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {

                            AsyncImage(
                                request = ImageRequest(
                                    context = localPlatformContext,
                                    uri = articleData.authorAvatar,
                                    configBlock = configBlock,
                                ),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(end = 5.dp)
                                    .size(30.dp)
                                    .clip(RoundedCornerShape(15.dp))
                                    .border(
                                        border = BorderStroke(2.dp, Color.Black),
                                        shape = RoundedCornerShape(20.dp)
                                    )
                            )

                            Text(
                                text = articleData.authorName ?: "",
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }


                        Box {
                            Text(
                                text = articleData.createTime ?: "",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.subTextColor
                            )
                        }


                    }
                }

                Column(
                    Modifier
                        .verticalScroll(rememberScrollState())
                        .wrapContentHeight()
                ) {
                    Box(
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .clip(
                                shape = RoundedCornerShape(15.dp)
                            )
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Text(
                            modifier = Modifier.padding(10.dp),
                            text = "TIPS: 多平台Markdown文档解析展示目前没有找到特别好的的工具组件，" +
                                    "先展示源文档，后续等相关组件完善后再进行文档渲染",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Text(
                        text = articleContent,
                    )
                }
            }


        }


    }


}