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
    var currentTime: Long by mutableStateOf(0)
    var duration = 0L
    var currentItemIndex = -1
    var canNext by mutableStateOf(false)
    var canPrev by mutableStateOf(false)
}

@Composable
fun rememberPlayerState(): PlayerState {
    return remember {
        PlayerState()
    }
}