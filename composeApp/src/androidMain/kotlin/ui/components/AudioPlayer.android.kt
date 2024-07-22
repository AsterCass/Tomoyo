package ui.components

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.aster.yuno.tomoyo.MainActivity
import data.PlayerState

actual class AudioPlayer actual constructor(private val playerState: PlayerState) {

    private val mediaPlayer = ExoPlayer.Builder(MainActivity.appContext).build()

    private val listener = object : Player.Listener {

        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                Player.STATE_IDLE -> {
                }

                Player.STATE_BUFFERING -> {
                    playerState.isBuffering = true
                }

                Player.STATE_ENDED -> {
                }

                Player.STATE_READY -> {
                    playerState.isBuffering = false
                }
            }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            playerState.isPlaying = isPlaying
        }

    }

    init {
        mediaPlayer.addListener(listener)
        mediaPlayer.prepare()
    }



    actual fun start(url: String) {
        mediaPlayer.setMediaItem(MediaItem.fromUri(url))
        play()
    }

    actual fun play() {
        if (playerState.isPlaying) return
        playerState.isPlaying = true
        mediaPlayer.play()
    }

    actual fun pause() {
        if (!playerState.isPlaying) return
        playerState.isPlaying = false
        mediaPlayer.pause()
    }

    actual fun next() {
    }

    actual fun prev() {
    }

    actual fun play(songIndex: Int) {
    }

    actual fun seekTo(time: Double) {
        playerState.currentTime = time
        mediaPlayer.seekTo((time * 1000).toLong())
    }

    actual fun addSongsUrls(songsUrl: List<String>) {
    }

    actual fun cleanUp() {
    }
}