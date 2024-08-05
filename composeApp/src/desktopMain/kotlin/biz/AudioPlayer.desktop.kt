package biz

import constant.enums.MusicPlayModel
import data.MusicPlayerState
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.util.Duration
import java.net.URL
import kotlin.random.Random

actual class AudioPlayer actual constructor(private val musicPlayerState: MusicPlayerState) {

    private var media: Media? = null

    private var mediaPlayer: MediaPlayer? = null

    private var currentItemIndex = -1

    private val mediaItems = mutableListOf<String>()

    actual fun start(index: Int) {
        //check
        if (index >= mediaItems.size || index < 0) return
        currentItemIndex = index
        playWithIndex(index)
    }

    actual fun play() {
        if (musicPlayerState.isPlaying) return
        try {
            musicPlayerState.isPlaying = true
            mediaPlayer?.play()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    actual fun pause() {
        if (!musicPlayerState.isPlaying) return
        try {
            musicPlayerState.isPlaying = false
            mediaPlayer?.pause()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    actual fun next() {
        when (musicPlayerState.playModel) {
            MusicPlayModel.ORDER.ordinal -> {
                currentItemIndex = currentItemIndex.plus(1).rem(mediaItems.size)
            }

            MusicPlayModel.RANDOM.ordinal -> {
                currentItemIndex = Random.nextInt(mediaItems.size)
            }

            MusicPlayModel.CIRCULATION.ordinal -> {

            }
            else -> {}
        }
        playWithIndex(currentItemIndex)
    }

    actual fun prev() {

    }

    actual fun seekTo(time: Double) {
        musicPlayerState.currentTime = time
        mediaPlayer?.seek(Duration.seconds(time))
    }

    actual fun addSongList(songsUrl: List<String>) {
        mediaItems += songsUrl
    }

    actual fun clearSongs() {
        mediaItems.clear()
    }

    actual fun cleanUp() {
        musicPlayerState.toBack()
        mediaPlayer?.stop()
    }

    private fun playWithIndex(index: Int) {
        musicPlayerState.currentIndex = index
        if (index >= mediaItems.size || index < 0) return
        val playItem = mediaItems[index]
        //close
        musicPlayerState.toBack()
        mediaPlayer?.stop()
        //start
        val thisUrl = URL(playItem)
        media = Media(thisUrl.toString())
        mediaPlayer = MediaPlayer(media)
        mediaPlayer?.statusProperty()?.addListener { _, oldStatus, newStatus ->
            if (newStatus === MediaPlayer.Status.READY
                && mediaPlayer?.totalDuration?.toSeconds()?.isNaN() != true
            ) {
                musicPlayerState.totalDuration =
                    mediaPlayer?.totalDuration?.toSeconds() ?: Short.MAX_VALUE.toDouble()
                play()
            }
            if (newStatus === MediaPlayer.Status.STALLED) {
                musicPlayerState.isBuffering = true
            }
            if (oldStatus === MediaPlayer.Status.STALLED
                && newStatus !== MediaPlayer.Status.STALLED
            ) {
                musicPlayerState.isBuffering = false
            }
        }

        mediaPlayer?.currentTimeProperty()?.addListener { _, _, newTime ->
            if (newTime.toSeconds() > musicPlayerState.currentTime + 1) {
                musicPlayerState.currentTime = newTime.toSeconds()
            }
        }

        mediaPlayer?.onEndOfMedia = Runnable {
            next()
        }
    }


}