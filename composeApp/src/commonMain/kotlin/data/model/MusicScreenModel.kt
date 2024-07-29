package data.model

import cafe.adriel.voyager.core.model.ScreenModel
import data.MusicPlayerState
import data.MusicSimpleModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import biz.AudioPlayer

class MusicScreenModel : ScreenModel {

    private val _playerState = MutableStateFlow(MusicPlayerState())
    val playerState = _playerState.asStateFlow()

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

    val onStart = { url: String -> _player.value.start(url) }
    val onPlay = { _player.value.play() }
    val onPause = { _player.value.pause() }


}