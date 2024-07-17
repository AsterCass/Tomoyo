package ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun MediaPlayer(modifier: Modifier, url: String) {

    Column {
        Text("目前版本对于JavaFx以及vlcj、包括Swing的支持都很差，windows端暂时不支持视频播放")
        Text("")
    }

}



