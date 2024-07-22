package ui.components

import data.PlayerState
import javazoom.jl.player.advanced.AdvancedPlayer
import java.io.IOException
import java.net.URL
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.SourceDataLine


actual class AudioPlayer actual constructor(private val playerState: PlayerState) {


    private var advancedPlayer: AdvancedPlayer? = null

    actual fun start(url: String) {
        //close
        advancedPlayer?.close()
        //start
        val thisUrl = URL(url)
        advancedPlayer = AdvancedPlayer(thisUrl.openStream())
    }

    actual suspend fun play() {
        if (playerState.isPlaying) return
        try {
            playerState.isPlaying = true
            advancedPlayer?.play()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    actual suspend fun pause() {
        if (!playerState.isPlaying) return
        try {
            playerState.isPlaying = false
            advancedPlayer?.close()
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
    }

    actual fun addSongsUrls(songsUrl: List<String>) {
    }

    actual fun cleanUp() {
    }

    private fun getOutFormat(inFormat: AudioFormat): AudioFormat {
        val ch = inFormat.channels

        val rate = inFormat.sampleRate
        return AudioFormat(PCM_SIGNED, rate, 16, ch, ch * 2, rate, false)
    }

    @Throws(IOException::class)
    private fun stream(`in`: AudioInputStream, line: SourceDataLine) {
        val buffer = ByteArray(4096)
        var n = 0
        while (n != -1) {
            line.write(buffer, 0, n)
            n = `in`.read(buffer, 0, buffer.size)
        }
    }


}