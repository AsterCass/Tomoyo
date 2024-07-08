package ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
expect fun MediaPlayer(modifier: Modifier, url: String)


interface MediaPlayer {
    fun play(url: String)
    fun pause()
    fun stop()
}