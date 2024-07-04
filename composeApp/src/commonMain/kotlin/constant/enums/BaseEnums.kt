package constant.enums

import org.jetbrains.compose.resources.StringResource
import tomoyo.composeapp.generated.resources.Res
import tomoyo.composeapp.generated.resources.app_name
import tomoyo.composeapp.generated.resources.articles
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
    SETTING("settings", Res.string.settings, Res.string.app_name),

}