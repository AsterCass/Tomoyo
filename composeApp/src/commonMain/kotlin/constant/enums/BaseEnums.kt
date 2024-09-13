package constant.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.Tab
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.AddressBook
import compose.icons.fontawesomeicons.solid.Book
import compose.icons.fontawesomeicons.solid.CandyCane
import compose.icons.fontawesomeicons.solid.Film
import compose.icons.fontawesomeicons.solid.ListUl
import compose.icons.fontawesomeicons.solid.Music
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import tomoyo.composeapp.generated.resources.Res
import tomoyo.composeapp.generated.resources.articles
import tomoyo.composeapp.generated.resources.contacts
import tomoyo.composeapp.generated.resources.home
import tomoyo.composeapp.generated.resources.media_repeat_all
import tomoyo.composeapp.generated.resources.media_repeat_one
import tomoyo.composeapp.generated.resources.media_shuffle
import tomoyo.composeapp.generated.resources.musics
import tomoyo.composeapp.generated.resources.none
import tomoyo.composeapp.generated.resources.play_audio_tab_favorite
import tomoyo.composeapp.generated.resources.play_audio_tab_private
import tomoyo.composeapp.generated.resources.play_audio_tab_public
import tomoyo.composeapp.generated.resources.play_model_circle
import tomoyo.composeapp.generated.resources.play_model_order
import tomoyo.composeapp.generated.resources.play_model_random
import tomoyo.composeapp.generated.resources.settings
import tomoyo.composeapp.generated.resources.videos
import ui.components.ArticlesTab
import ui.components.ContactsTab
import ui.components.HomeTab
import ui.components.MusicsTab
import ui.components.SettingTab
import ui.components.VideosTab


enum class MusicPlayScreenTabModel(
    val text: StringResource,
    val collectionId: String,
) {
    COMMON(Res.string.play_audio_tab_public, "0"),
    FAV(Res.string.play_audio_tab_favorite, "-1"),
    COLLECTIONS(Res.string.play_audio_tab_private, "-2"),
}

enum class MusicPlayModel(
    val imageVector: DrawableResource,
    val desc: StringResource,
) {
    ORDER(Res.drawable.media_repeat_all, Res.string.play_model_order),
    CIRCULATION(Res.drawable.media_repeat_one, Res.string.play_model_circle),
    RANDOM(Res.drawable.media_shuffle, Res.string.play_model_random),
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

    HOME("home", Res.string.home, FontAwesomeIcons.Solid.CandyCane, HomeTab),
    ARTICLES("articles", Res.string.articles, FontAwesomeIcons.Solid.Book, ArticlesTab),
    MUSICS("musics", Res.string.musics, FontAwesomeIcons.Solid.Music, MusicsTab),
    VIDEOS("videos", Res.string.videos, FontAwesomeIcons.Solid.Film, VideosTab),
    Contacts("contacts", Res.string.contacts, FontAwesomeIcons.Solid.AddressBook, ContactsTab),
    SETTING("settings", Res.string.settings, FontAwesomeIcons.Solid.ListUl, SettingTab),


    MUSIC_PLAYER("music_player", Res.string.none, Icons.Rounded.Done, HomeTab);


    companion object {
        fun getEnumByCode(code: String): MainNavigationEnum {
            var ret = HOME
            for (thisEnum in entries) {
                if (thisEnum.code == code) {
                    ret = thisEnum
                    break
                }
            }
            return ret
        }
    }
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