package constant.enums

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.StringResource
import tomoyo.composeapp.generated.resources.Res
import tomoyo.composeapp.generated.resources.app_name
import tomoyo.composeapp.generated.resources.articles
import tomoyo.composeapp.generated.resources.chat
import tomoyo.composeapp.generated.resources.home
import tomoyo.composeapp.generated.resources.musics
import tomoyo.composeapp.generated.resources.settings
import tomoyo.composeapp.generated.resources.videos

enum class MainNavigationEnum(
    val code: String,
    val title: StringResource,
    val icon: StringResource,
) {

    HOME("home", Res.string.home, Res.string.app_name),
    ARTICLES("articles", Res.string.articles, Res.string.app_name),
    MUSICS("musics", Res.string.musics, Res.string.app_name),
    VIDEOS("videos", Res.string.videos, Res.string.app_name),
    CHAT("chat", Res.string.chat, Res.string.app_name),
    SETTING("settings", Res.string.settings, Res.string.app_name),

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