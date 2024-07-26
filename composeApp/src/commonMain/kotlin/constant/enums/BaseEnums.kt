package constant.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.Tab
import org.jetbrains.compose.resources.StringResource
import tomoyo.composeapp.generated.resources.Res
import tomoyo.composeapp.generated.resources.articles
import tomoyo.composeapp.generated.resources.chat
import tomoyo.composeapp.generated.resources.home
import tomoyo.composeapp.generated.resources.musics
import tomoyo.composeapp.generated.resources.none
import tomoyo.composeapp.generated.resources.settings
import tomoyo.composeapp.generated.resources.videos
import ui.components.ArticlesTab
import ui.components.HomeTab


enum class MusicPlayModel {
    CIRCULATION, ORDER, RANDOM
}

enum class NotificationType(
    val code: String,
    val bgColor: Color,
    val fontColor: Color,
) {
    SUCCESS("success", Color.Transparent, Color.Transparent),
    WARNING("warning", Color.Transparent, Color.Transparent),
    ERROR("error", Color.Transparent, Color.Transparent),
    TIP("tip", Color.Transparent, Color.Transparent),
}

enum class MainNavigationEnum(
    val code: String,
    val title: StringResource,
    val icon: ImageVector,
    val tab: Tab,
) {

//    HOME("home", Res.string.home, FontAwesomeIcons.Solid.CandyCane, HomeTab),
//    ARTICLES("articles", Res.string.articles, FontAwesomeIcons.Solid.Book, ArticlesTab),
//    MUSICS("musics", Res.string.musics, FontAwesomeIcons.Solid.Music, HomeTab),
//    VIDEOS("videos", Res.string.videos, FontAwesomeIcons.Solid.Film, HomeTab),
//    CHAT("chat", Res.string.chat, FontAwesomeIcons.Solid.Comments, HomeTab),
//    SETTING("settings", Res.string.settings, FontAwesomeIcons.Solid.ListUl, HomeTab),


    HOME("home", Res.string.home, Icons.Rounded.PlayArrow, HomeTab),
    ARTICLES("articles", Res.string.articles, Icons.Rounded.PlayArrow, ArticlesTab),
    MUSICS("musics", Res.string.musics, Icons.Rounded.PlayArrow, HomeTab),
    VIDEOS("videos", Res.string.videos, Icons.Rounded.PlayArrow, HomeTab),
    CHAT("chat", Res.string.chat, Icons.Rounded.PlayArrow, HomeTab),
    SETTING("settings", Res.string.settings, Icons.Rounded.PlayArrow, HomeTab),


    MUSIC_PLAYER("music_player", Res.string.none, Icons.Rounded.Done, HomeTab)
}


enum class WindowsSizeEnum(
    val code: String,
    val title: String,
    val data: DpSize,
) {

    LOW("480", "640x480", DpSize(width = 640.dp, height = 480.dp)),
    STD("720", "1280x720", DpSize(width = 1280.dp, height = 720.dp)),
    HIGH("1080", "1920x1080", DpSize(width = 1920.dp, height = 1080.dp)),
    VH("2k", "2k", DpSize(width = 2560.dp, height = 1440.dp)),

}