package ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.jetbrains.compose.resources.stringResource
import tomoyo.composeapp.generated.resources.Res
import tomoyo.composeapp.generated.resources.articles
import tomoyo.composeapp.generated.resources.home
import ui.pages.MainArticleScreen
import ui.pages.MainHomeScreen


object HomeTab : Tab {
//        private fun readResolve(): Any = HomeTab

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(Res.string.home)
            val icon = rememberVectorPainter(Icons.Default.Home)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
//                        icon = icon,
                )
            }
        }

    @Composable
    override fun Content() {
        MainHomeScreen()
    }
}


object ArticlesTab : Tab {
//        private fun readResolve(): Any = ArticlesTab

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(Res.string.articles)
            val icon = rememberVectorPainter(Icons.Default.Home)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon,
                )
            }
        }

    @Composable
    override fun Content() {
//        val screenModel = koinScreenModel<ArticleScreenModel>()
//        val state by screenModel.openBottomSheet.collectAsState()
        MainArticleScreen(
//            state
        )
    }

}