package data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import constant.enums.MusicPlayModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class AudioSimpleModel(
    @SerialName("id")
    val id: String = "",
    val audioCollectionId: String? = null,
    val audioOrder: Int = 1,
    val audioName: String = "",
    val audioImg: String? = null,
    val audioBrief: String? = null,
    val audioAuthor: String = "",
    val audioUrl: String = "",
)

@Stable
class MusicPlayerState {
    var isPlaying by mutableStateOf(false)
        internal set
    var isBuffering by mutableStateOf(false)
    var currentTime by mutableStateOf(0.0)
    var totalDuration by mutableStateOf(0.0)

    var currentPlayId by mutableStateOf("")
    var currentCollectionId by mutableStateOf("")
    var playModel by mutableStateOf(MusicPlayModel.ORDER.ordinal)

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

