package ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import data.model.MainScreenModel


data class ArticleScreen(
    val articleId: String,
    val mainModel: MainScreenModel,
) : Screen {

    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {

        val loadingScreen = mainModel.loadingScreen.collectAsState().value

        AnimatedVisibility(
            visible = !loadingScreen,
            enter = fadeIn(animationSpec = tween(durationMillis = 1000)) + scaleIn(
                initialScale = 0.2f, animationSpec = tween(durationMillis = 1000),
            ),
        ) {
            Text("123456")
        }


//        val webViewState = rememberWebViewStateWithHTMLData(
//            data = """
//                <html>
//                    <body>
//                        <h1>Hello World</h1>
//                    </body>
//                </html>
//        """.trimIndent()
//        )
//        WebView(
//            state = webViewState,
//            modifier = Modifier.fillMaxSize()
//        )
//        showAnimation = true
//        LaunchedEffect(Unit) {
//            delay(500)
//            showAnimation = true
//        }

    }


}