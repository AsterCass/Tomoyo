package ui.components

import data.PlayerState
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.util.Duration
import java.net.URL


actual class AudioPlayer actual constructor(private val playerState: PlayerState) {

    private var media: Media? = null

    private var mediaPlayer: MediaPlayer? = null

    actual fun start(url: String) {
        //close
        playerState.toBack()
        mediaPlayer?.stop()
        //start
        val thisUrl = URL(url)
        media = Media(thisUrl.toString())
        mediaPlayer = MediaPlayer(media)
        mediaPlayer?.statusProperty()?.addListener { _, oldStatus, newStatus ->
            if (newStatus === MediaPlayer.Status.READY) {
                playerState.totalDuration = mediaPlayer?.totalDuration?.toSeconds() ?: 0.0
                play()
            }
            if (newStatus === MediaPlayer.Status.STALLED) {
                playerState.isBuffering = true
            }
            if (oldStatus === MediaPlayer.Status.STALLED
                && newStatus !== MediaPlayer.Status.STALLED
            ) {
                playerState.isBuffering = false
            }
        }
        mediaPlayer?.currentTimeProperty()?.addListener { _, _, newTime ->
            if (newTime.toSeconds() > playerState.currentTime + 1) {
                playerState.currentTime = newTime.toSeconds()
            }
        }
    }

    actual fun play() {
        if (playerState.isPlaying) return
        try {
            playerState.isPlaying = true
            mediaPlayer?.play()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    actual fun pause() {
        if (!playerState.isPlaying) return
        try {
            playerState.isPlaying = false
            mediaPlayer?.pause()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    actual fun next() {
    }

    actual fun prev() {

    }

    actual fun play(songIndex: Int) {
    }

    actual fun seekTo(time: Double) {
        mediaPlayer?.seek(Duration.seconds(time))
    }

    actual fun addSongsUrls(songsUrl: List<String>) {
    }

    actual fun cleanUp() {
    }


}