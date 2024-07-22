package data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import constant.enums.MainNavigationEnum

data class PlatformInitData(
    val extraNavigationList: List<MainNavigationEnum> = emptyList(),
)

@Stable
class PlayerState {
    var isPlaying by mutableStateOf(false)
        internal set
    var isBuffering by mutableStateOf(false)
    var currentTime by mutableStateOf(0.0)
    var totalDuration by mutableStateOf(0.0)

    fun toBack() {
        isPlaying = false
        isBuffering = false
        currentTime = 0.0
        totalDuration = 0.0
    }
}

@Composable
fun rememberPlayerState(): PlayerState {
    return remember {
        PlayerState()
    }
}