package ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import constant.enums.ViewEnum
import ui.components.MediaPlayer


object MainVideosScreen : Screen {

    override val key: ScreenKey = "${ViewEnum.TAB_MAIN_VIDEOS.code}$uniqueScreenKey"

    @Composable
    override fun Content() {
        MainVideosScreen()
    }

}


@Composable
fun MainVideosScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            MediaPlayer(
                modifier = Modifier.fillMaxWidth().height(400.dp),
                url =
                ""
            )


        }
    }
}