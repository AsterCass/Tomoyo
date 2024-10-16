package biz

import data.AudioSimpleModel
import data.MusicPlayerState

actual class AudioPlayer actual constructor(private val musicPlayerState: MusicPlayerState) {

    actual fun start(id: String) {

    }

    actual fun play() {

    }

    actual fun pause() {

    }

    actual fun next() {

    }

    actual fun prev() {

    }

    actual fun seekTo(time: Double) {

    }

    actual fun addSongList(songs: Map<String, AudioSimpleModel>) {

    }

    actual fun clearSongs() {

    }

    actual fun cleanUp() {

    }


}