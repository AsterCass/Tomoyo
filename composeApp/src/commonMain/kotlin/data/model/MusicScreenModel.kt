package data.model

import biz.AudioPlayer
import cafe.adriel.voyager.core.model.ScreenModel
import data.MusicPlayerState
import data.MusicSimpleModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MusicScreenModel : ScreenModel {

    private val _playerState = MutableStateFlow(MusicPlayerState())
    val playerState = _playerState.asStateFlow()

    private val _playingListId = MutableStateFlow("")
    private val _player = MutableStateFlow(AudioPlayer(_playerState.value))
    private val _musicPlayList = MutableStateFlow<List<MusicSimpleModel>>(
//        emptyList()
        listOf(
            MusicSimpleModel(
                id = "1",
                musicName = "有人",
                musicAuthor = "不大萌",
                musicUrl = "https://api.astercasc.com/ushio/video-pro/audios/1723108411781?expiry=1&signature=1",
            ),
            MusicSimpleModel(
                id = "2",
                musicName = "生僻字",
                musicAuthor = "陈柯",
                musicUrl = "https://api.astercasc.com/ushio/video-pro/audios/1722178591590?expiry=1&signature=1",
            ),
            MusicSimpleModel(
                id = "3",
                musicName = "起风了",
                musicAuthor = "Mukoyo木西",
                musicUrl = "https://api.astercasc.com/ushio/video-pro/audios/1720841179970?expiry=1&signature=1",
            ),
        )
    )


    val musicPlayList = _musicPlayList.asStateFlow()

    fun onStart(
        index: Int, playListId: String,
        playList: List<MusicSimpleModel> = emptyList()
    ) {
        if (playListId != _playingListId.value && playList.isNotEmpty()) {
            _player.value.clearSongs()
            _player.value.addSongList(playList.map { it.musicUrl ?: "" })
            _playingListId.value = playListId
            _musicPlayList.value = playList
        }
        _player.value.start(index)
    }

    fun onPlay() {
        _player.value.play()
    }

    fun onPause() {
        _player.value.pause()
    }

    fun onSeek(time: Double) {
        _player.value.seekTo(time)
    }



}