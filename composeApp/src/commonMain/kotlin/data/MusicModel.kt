package data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.serialization.Serializable

@Serializable
data class MusicSimpleModel(
    val id: String? = null,
    val musicName: String? = null,
    val musicAuthor: String? = null,
    val musicUrl: String? = null,
)

@Stable
class MusicPlayerState {
    var isPlaying by mutableStateOf(false)
        internal set
    var isBuffering by mutableStateOf(false)
    var currentTime by mutableStateOf(0.0)
    var totalDuration by mutableStateOf(0.0)

    var nextIndex = Pair(-1, "")
    var preIndex = Pair(-1, "")

    fun toBack() {
        isPlaying = false
        isBuffering = false
        currentTime = 0.0
        totalDuration = 0.0
    }
}

@Composable
fun rememberMusicPlayerState(): MusicPlayerState {
    return remember {
        MusicPlayerState()
    }
}

