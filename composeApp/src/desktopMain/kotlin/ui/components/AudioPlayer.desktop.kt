package ui.components

import data.MusicPlayerState
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.util.Duration
import java.net.URL


actual class AudioPlayer actual constructor(private val musicPlayerState: MusicPlayerState) {

    private var media: Media? = null

    private var mediaPlayer: MediaPlayer? = null

    actual fun start(url: String) {
        //close
        musicPlayerState.toBack()
        mediaPlayer?.stop()
        //start
        val thisUrl = URL(url)
        media = Media(thisUrl.toString())
        mediaPlayer = MediaPlayer(media)
        mediaPlayer?.statusProperty()?.addListener { _, oldStatus, newStatus ->
            if (newStatus === MediaPlayer.Status.READY) {
                musicPlayerState.totalDuration = mediaPlayer?.totalDuration?.toSeconds() ?: 0.0
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
    }

    actual fun prev() {

    }

    actual fun play(songIndex: Int) {
    }

    actual fun seekTo(time: Double) {
        musicPlayerState.currentTime = time
        mediaPlayer?.seek(Duration.seconds(time))
    }

    actual fun addSongsUrls(songsUrl: List<String>) {
    }

    actual fun cleanUp() {
    }


}