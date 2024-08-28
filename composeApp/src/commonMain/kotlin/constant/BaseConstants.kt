package constant

import androidx.compose.ui.graphics.Color
import constant.enums.MainNavigationEnum

const val BASE_SERVER_ADDRESS = "https://api.astercasc.com"

val FULL_SCREEN_ROUTES = listOf(MainNavigationEnum.MUSIC_PLAYER.name)


object BaseResText {
    var userNoLogin: String = ""
    var underDevelopment: String = ""
    var bgColorList: List<Color> = listOf(Color.White, Color.White, Color.White)
}
