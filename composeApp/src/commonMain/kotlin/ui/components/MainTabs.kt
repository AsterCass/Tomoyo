package ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import constant.enums.MainNavigationEnum
import org.jetbrains.compose.resources.stringResource
import ui.pages.MainArticleScreen
import ui.pages.MainChatScreen
import ui.pages.MainHomeScreen
import ui.pages.MainMusicsScreen
import ui.pages.MainSettingsScreen
import ui.pages.MainVideosScreen


object HomeTab : Tab {
    private fun readResolve(): Any = HomeTab

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(MainNavigationEnum.HOME.title)
            val icon = rememberVectorPainter(MainNavigationEnum.HOME.icon)

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
        MainHomeScreen()
    }
}


object ArticlesTab : Tab {
    private fun readResolve(): Any = ArticlesTab

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(MainNavigationEnum.ARTICLES.title)
            val icon = rememberVectorPainter(MainNavigationEnum.ARTICLES.icon)

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
        MainArticleScreen()
    }

}

object MusicsTab : Tab {
    private fun readResolve(): Any = MusicsTab


    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(MainNavigationEnum.MUSICS.title)
            val icon = rememberVectorPainter(MainNavigationEnum.MUSICS.icon)

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
        MainMusicsScreen()
    }

}

object VideosTab : Tab {
    private fun readResolve(): Any = VideosTab

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(MainNavigationEnum.VIDEOS.title)
            val icon = rememberVectorPainter(MainNavigationEnum.VIDEOS.icon)

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
        MainVideosScreen()
    }

}

object ChatTab : Tab {
    private fun readResolve(): Any = ChatTab

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(MainNavigationEnum.CHAT.title)
            val icon = rememberVectorPainter(MainNavigationEnum.CHAT.icon)

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
        MainChatScreen()
    }

}

object SettingTab : Tab {
    private fun readResolve(): Any = SettingTab


    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(MainNavigationEnum.SETTING.title)
            val icon = rememberVectorPainter(MainNavigationEnum.SETTING.icon)

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
        MainSettingsScreen()
    }

}