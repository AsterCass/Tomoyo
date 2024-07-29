package biz

import data.MusicPlayerState

expect class AudioPlayer(musicPlayerState: MusicPlayerState) {
    fun start(url: String)
    fun play()
    fun pause()
    fun next()
    fun prev()
    fun play(songIndex: Int)
    fun seekTo(time: Double)
    fun addSongsUrls(songsUrl: List<String>)
    fun cleanUp()
}