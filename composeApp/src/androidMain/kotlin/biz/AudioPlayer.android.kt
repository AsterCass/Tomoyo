package biz

import android.os.Handler
import android.os.Looper
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.aster.yuno.tomoyo.MainActivity
import constant.enums.MusicPlayModel
import data.MusicPlayerState

actual class AudioPlayer actual constructor(
    private val musicPlayerState: MusicPlayerState,
) : Runnable {

    private val handler = Handler(Looper.getMainLooper())

    private val mediaPlayer = ExoPlayer.Builder(MainActivity.mainContext!!).build()

    private val mediaItems = mutableListOf<MediaItem>()

    private var currentItemIndex = -1

    private val listener = object : Player.Listener {

        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                Player.STATE_IDLE -> {
                }

                Player.STATE_BUFFERING -> {
                    musicPlayerState.isBuffering = true
                }

                Player.STATE_ENDED -> {
                    if (musicPlayerState.isPlaying) {
                        next()
                    }
                }

                Player.STATE_READY -> {
                    musicPlayerState.isBuffering = false
                    musicPlayerState.totalDuration = (mediaPlayer.duration / 1000).toDouble()
                }
            }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            musicPlayerState.isPlaying = isPlaying
            if (isPlaying) scheduleUpdate() else stopUpdate()
        }

    }

    init {
        mediaPlayer.addListener(listener)
        mediaPlayer.prepare()
    }

    actual fun start(index: Int) {
        if (index >= mediaItems.size) return
        currentItemIndex = index
        playWithIndex(index)
    }

    actual fun play() {
        if (musicPlayerState.isPlaying) return
        mediaPlayer.play()
    }

    actual fun pause() {
        if (!musicPlayerState.isPlaying) return
        mediaPlayer.pause()
    }

    actual fun next() {
        when (musicPlayerState.playModel) {
            MusicPlayModel.ORDER -> {
                currentItemIndex = currentItemIndex.plus(1).rem(mediaItems.size)
            }

            else -> {}
        }
        playWithIndex(currentItemIndex)
    }

    actual fun prev() {

    }

    actual fun seekTo(time: Double) {
        musicPlayerState.currentTime = time
        if (musicPlayerState.totalDuration - musicPlayerState.currentTime < 1) {
            next()
        } else {
            mediaPlayer.seekTo((time * 1000).toLong())
        }
    }

    actual fun addSongList(songsUrl: List<String>) {
        mediaItems += songsUrl.map { MediaItem.fromUri(it) }
    }

    actual fun cleanUp() {
        mediaPlayer.release()
        mediaPlayer.removeListener(listener)
    }

    actual fun clearSongs() {
        mediaItems.clear()
    }

    override fun run() {
        musicPlayerState.currentTime = (mediaPlayer.currentPosition / 1000).toDouble()
        handler.postDelayed(this, 500)
    }

    private fun stopUpdate() {
        handler.removeCallbacks(this)
    }

    private fun scheduleUpdate() {
        stopUpdate()
        handler.postDelayed(this, 100)
    }

    private fun playWithIndex(index: Int) {
        musicPlayerState.currentIndex = index
        if (index >= 0) {
            val playItem = mediaItems[index]
            mediaPlayer.setMediaItem(playItem)
            mediaPlayer.play()
        }
    }

}