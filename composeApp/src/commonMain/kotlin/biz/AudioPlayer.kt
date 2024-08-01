package biz

import data.MusicPlayerState

expect class AudioPlayer(musicPlayerState: MusicPlayerState) {
    fun start(index: Int)
    fun play()
    fun pause()
    fun next()
    fun prev()
    fun seekTo(time: Double)
    fun cleanUp()

    fun clearSongs()
    fun addSongList(songsUrl: List<String>)
}