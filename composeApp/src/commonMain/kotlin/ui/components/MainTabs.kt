package ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import constant.enums.MainNavigationEnum
import org.jetbrains.compose.resources.stringResource
import ui.pages.MainArticlesScreen
import ui.pages.MainChatScreen
import ui.pages.MainContactsScreen
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
            val index = MainNavigationEnum.HOME.ordinal.toUShort()

            return remember {
                TabOptions(
                    index = index,
                    title = title,
                    icon = icon,
                )
            }
        }

    @Composable
    override fun Content() {
        Navigator(screen = MainHomeScreen)
    }
}


object ArticlesTab : Tab {
    private fun readResolve(): Any = ArticlesTab

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(MainNavigationEnum.ARTICLES.title)
            val icon = rememberVectorPainter(MainNavigationEnum.ARTICLES.icon)
            val index = MainNavigationEnum.ARTICLES.ordinal.toUShort()

            return remember {
                TabOptions(
                    index = index,
                    title = title,
                    icon = icon,
                )
            }
        }

    @Composable
    override fun Content() {
        Navigator(screen = MainArticlesScreen)
    }

}

object MusicsTab : Tab {
    private fun readResolve(): Any = MusicsTab


    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(MainNavigationEnum.MUSICS.title)
            val icon = rememberVectorPainter(MainNavigationEnum.MUSICS.icon)
            val index = MainNavigationEnum.MUSICS.ordinal.toUShort()

            return remember {
                TabOptions(
                    index = index,
                    title = title,
                    icon = icon,
                )
            }
        }

    @Composable
    override fun Content() {
        Navigator(screen = MainMusicsScreen)
    }

}

object VideosTab : Tab {
    private fun readResolve(): Any = VideosTab

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(MainNavigationEnum.VIDEOS.title)
            val icon = rememberVectorPainter(MainNavigationEnum.VIDEOS.icon)
            val index = MainNavigationEnum.VIDEOS.ordinal.toUShort()

            return remember {
                TabOptions(
                    index = index,
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
            val index = MainNavigationEnum.CHAT.ordinal.toUShort()

            return remember {
                TabOptions(
                    index = index,
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

object ContactsTab : Tab {

    private fun readResolve(): Any = ContactsTab

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(MainNavigationEnum.Contacts.title)
            val icon = rememberVectorPainter(MainNavigationEnum.Contacts.icon)
            val index = MainNavigationEnum.Contacts.ordinal.toUShort()

            return remember {
                TabOptions(
                    index = index,
                    title = title,
                    icon = icon,
                )
            }
        }


    @Composable
    override fun Content() {
        Navigator(screen = MainContactsScreen)
    }

}

object SettingTab : Tab {
    private fun readResolve(): Any = SettingTab


    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(MainNavigationEnum.SETTING.title)
            val icon = rememberVectorPainter(MainNavigationEnum.SETTING.icon)
            val index = MainNavigationEnum.SETTING.ordinal.toUShort()

            return remember {
                TabOptions(
                    index = index,
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


