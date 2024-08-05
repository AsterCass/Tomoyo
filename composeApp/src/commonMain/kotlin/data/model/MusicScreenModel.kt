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

    private val _playingListId = MutableStateFlow("")
    private val _player = MutableStateFlow(AudioPlayer(_playerState.value))
    private val _musicPlayList = MutableStateFlow<List<AudioSimpleModel>>(emptyList())
    val musicPlayList = _musicPlayList.asStateFlow()
    suspend fun updateAllAudioList() {
        if (_musicPlayList.value.isNotEmpty()) {
            return
        }
        _musicPlayList.value = BaseApi().getAllAudio()
    }

    fun nextPlayModel() {
        _playerState.value.playModel = _playerState.value.playModel
            .plus(1).rem(MusicPlayModel.entries.size)
    }

    fun onStart(
        index: Int, playListId: String,
        playList: List<AudioSimpleModel> = _musicPlayList.value
    ) {
        if (playListId != _playingListId.value && playList.isNotEmpty()) {
            _player.value.clearSongs()
            //todo get auth url
            _player.value.addSongList(playList.map {
                "https://api.astercasc.com/ushio/video-pro/audios/${it.id}?expiry=1&signature=1"
            })
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

    fun onNext() {
        _player.value.next()
    }

    fun onPrev() {
        _player.value.prev()
    }



}