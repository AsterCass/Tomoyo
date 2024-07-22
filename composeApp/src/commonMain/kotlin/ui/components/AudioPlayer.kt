package ui.components

import data.PlayerState

expect class AudioPlayer(playerState: PlayerState) {
    fun start(url: String)
    suspend fun play()
    suspend fun pause()
    fun next()
    fun prev()
    fun play(songIndex: Int)
    fun seekTo(time: Double)
    fun addSongsUrls(songsUrl: List<String>)
    fun cleanUp()
}