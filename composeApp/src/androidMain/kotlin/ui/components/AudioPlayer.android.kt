package ui.components

import data.PlayerState

actual class AudioPlayer actual constructor(playerState: PlayerState) {

    actual fun start(url: String) {
    }

    actual fun play() {
    }

    actual fun pause() {
    }

    actual fun next() {
    }

    actual fun prev() {
    }

    actual fun play(songIndex: Int) {
    }

    actual fun seekTo(time: Double) {
    }

    actual fun addSongsUrls(songsUrl: List<String>) {
    }

    actual fun cleanUp() {
    }
}