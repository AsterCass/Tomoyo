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

    private val _playingIndex = MutableStateFlow(0)
    private val _player = MutableStateFlow(AudioPlayer(_playerState.value))
    private val _musicPlayList = MutableStateFlow(
        listOf<MusicSimpleModel>(
            MusicSimpleModel(
                id = "1",
                musicName = "歌曲1",
                musicAuthor = "张三1",
                musicUrl = "https://astercasc-web-admin-1256368017.cos.ap-shanghai.myqcloud.com/test/1.m4a",
            ),
            MusicSimpleModel(
                id = "2",
                musicName = "歌曲2",
                musicAuthor = "张三2",
                musicUrl = "https://astercasc-web-admin-1256368017.cos.ap-shanghai.myqcloud.com/test/2.m4a",
            ),
            MusicSimpleModel(
                id = "3",
                musicName = "歌曲3",
                musicAuthor = "张三3",
                musicUrl = "https://astercasc-web-admin-1256368017.cos.ap-shanghai.myqcloud.com/test/1.mp3",
            ),
        )
    )


    val musicPlayList = _musicPlayList.asStateFlow()

    fun onStart(index: Int, url: String) {
        _playingIndex.value = index
        _player.value.start(url)
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
        if (_musicPlayList.value.isNotEmpty()) {
            val newIndex = _playingIndex.value.plus(1).rem(_musicPlayList.value.size)
            onStart(newIndex, _musicPlayList.value[newIndex].musicUrl ?: "")
        }
    }


}