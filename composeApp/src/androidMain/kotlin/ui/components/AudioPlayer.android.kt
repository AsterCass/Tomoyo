package ui.components

import android.os.Handler
import android.os.Looper
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.aster.yuno.tomoyo.MainActivity
import data.PlayerState

actual class AudioPlayer actual constructor(
    private val playerState: PlayerState,
) : Runnable {

    private val handler = Handler(Looper.getMainLooper())

    private val mediaPlayer = ExoPlayer.Builder(MainActivity.mainContext!!).build()

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
                    playerState.totalDuration = (mediaPlayer.duration / 1000).toDouble()
                }
            }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            playerState.isPlaying = isPlaying
            if (isPlaying) scheduleUpdate() else stopUpdate()
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
        mediaPlayer.play()
    }

    actual fun pause() {
        if (!playerState.isPlaying) return
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

    override fun run() {
        println("do something")
        playerState.currentTime = (mediaPlayer.currentPosition / 1000).toDouble()
        handler.postDelayed(this, 1000)
    }

    private fun stopUpdate() {
        handler.removeCallbacks(this)
    }

    private fun scheduleUpdate() {
        stopUpdate()
        handler.postDelayed(this, 100)
    }

}