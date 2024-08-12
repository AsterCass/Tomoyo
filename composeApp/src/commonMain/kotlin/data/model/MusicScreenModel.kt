package data.model

import api.BaseApi
import api.baseJsonConf
import biz.AudioPlayer
import cafe.adriel.voyager.core.model.ScreenModel
import constant.enums.MusicPlayModel
import constant.enums.MusicPlayScreenTabModel
import data.AudioSimpleModel
import data.MusicPlayerState
import data.store.DataStorageManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.encodeToString

class MusicScreenModel(
    private val dataStorageManager: DataStorageManager
) : ScreenModel {

    //fav
    private val _favList = MutableStateFlow(setOf<String>())
    val favList = _favList.asStateFlow()
    fun addFav(id: String) {
        val newFav = mutableSetOf<String>()
        newFav.addAll(_favList.value)
        newFav.add(id)
        _favList.value = newFav
        dataStorageManager.setString(
            DataStorageManager.FAV_AUDIO_ID_LIST,
            baseJsonConf.encodeToString(newFav)
        )
        favUpdateToPlayer()
    }

    fun delFav(id: String) {
        val newFav = mutableSetOf<String>()
        _favList.value.forEach { if (it != id) newFav.add(it) }
        _favList.value = newFav
        dataStorageManager.setString(
            DataStorageManager.FAV_AUDIO_ID_LIST,
            baseJsonConf.encodeToString(newFav)
        )
        favUpdateToPlayer()
    }

    private fun favUpdateToPlayer() {
        if (_playerState.value.currentCollectionId == MusicPlayScreenTabModel.FAV.collectionId) {
            _player.value.clearSongs()
            _player.value.addSongList(_musicPlayMap.value
                .filter { _favList.value.contains(it.key) })
        }
    }

    //tab
    private val _musicTab = MutableStateFlow(MusicPlayScreenTabModel.COMMON)
    val musicTab = _musicTab.asStateFlow()
    fun updateMusicTab(tab: MusicPlayScreenTabModel) {
        _musicTab.value = tab
    }

    //player
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
        playCollectionId: String,
        musicPlayMap: Map<String, AudioSimpleModel> = _musicPlayMap.value
    ) {
        if (playListId == _playerState.value.currentPlayId) {
            return
        }
        //todo login check
        if (musicPlayMap.isNotEmpty() &&
            _playerState.value.currentCollectionId != playCollectionId
        ) {
            _playerState.value.currentCollectionId = playCollectionId
            _player.value.clearSongs()
            _player.value.addSongList(musicPlayMap)
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

    init {
        //fav
        val favList = dataStorageManager.getNonFlowString(
            DataStorageManager.FAV_AUDIO_ID_LIST
        )
        if (favList.isNotBlank()) {
            _favList.value = baseJsonConf.decodeFromString(favList)
        }

    }


}