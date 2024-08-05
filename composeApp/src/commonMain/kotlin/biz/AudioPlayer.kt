package biz

import data.AudioSimpleModel
import data.MusicPlayerState

expect class AudioPlayer(musicPlayerState: MusicPlayerState) {
    fun start(id: String)
    fun play()
    fun pause()
    fun next()
    fun prev()
    fun seekTo(time: Double)
    fun cleanUp()

    fun clearSongs()
    fun addSongList(songs: Map<String, AudioSimpleModel>)
}