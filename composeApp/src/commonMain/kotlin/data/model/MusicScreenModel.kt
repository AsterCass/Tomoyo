package data.model

import api.BaseApi
import biz.AudioPlayer
import cafe.adriel.voyager.core.model.ScreenModel
import constant.enums.MusicPlayModel
import data.AudioSimpleModel
import data.MusicPlayerState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MusicScreenModel : ScreenModel {

    private val _playerState = MutableStateFlow(MusicPlayerState())
    val playerState = _playerState.asStateFlow()

    private val _player = MutableStateFlow(AudioPlayer(_playerState.value))
    private val _musicPlayMap = MutableStateFlow<Map<String, AudioSimpleModel>>(emptyMap())
    val musicPlayMap = _musicPlayMap.asStateFlow()
    suspend fun updateAllAudioList() {
        if (_musicPlayMap.value.isNotEmpty()) {
            return
        }
        _musicPlayMap.value = BaseApi().getAllAudio().associateBy { it.id }
    }

    fun getCurrentMusicData() =
        _musicPlayMap.value.getOrDefault(_playerState.value.currentPlayId, AudioSimpleModel())

    fun nextPlayModel() {
        _playerState.value.playModel = _playerState.value.playModel
            .plus(1).rem(MusicPlayModel.entries.size)
    }

    fun onStart(
        playListId: String,
        musicPlayMap: Map<String, AudioSimpleModel> = _musicPlayMap.value
    ) {
        if (playListId == _playerState.value.currentPlayId) {
            return
        }
        //todo login check
        if (musicPlayMap.isNotEmpty()) {
            _player.value.clearSongs()
            _player.value.addSongList(musicPlayMap)
            _musicPlayMap.value = musicPlayMap
        }
        _player.value.start(playListId)
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

    fun onNext() {
        _player.value.next()
    }

    fun onPrev() {
        _player.value.prev()
    }


}