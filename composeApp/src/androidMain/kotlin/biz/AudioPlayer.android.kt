package biz

import android.os.Handler
import android.os.Looper
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.aster.yuno.tomoyo.MainActivity
import constant.enums.MusicPlayModel
import data.MusicPlayerState


//class MediaPlaybackService : Service() {
//
//    private lateinit var exoPlayer: ExoPlayer
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        // Setup ExoPlayer
//        exoPlayer = ExoPlayer.Builder(this).build()
//        exoPlayer.setAudioAttributes(
//            AudioAttributes.Builder()
//                .setContentType(C.CONTENT_TYPE_MUSIC)
//                .setUsage(C.USAGE_MEDIA)
//                .build(),
//            true
//        )
//
//        // Create and show a notification
//        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
//            .setContentTitle("Playing media")
//            .setContentText("Media is playing in the background")
//            .setSmallIcon(R.drawable.ic_notification)
//            .build()
//
//        startForeground(NOTIFICATION_ID, notification)
//
//        // Start playing media
//        exoPlayer.setMediaItem(/* Your media item here */)
//        exoPlayer.prepare()
//        exoPlayer.play()
//
//        return START_STICKY
//    }
//
//    override fun onDestroy() {
//        exoPlayer.release()
//        super.onDestroy()
//    }
//
//    override fun onBind(intent: Intent?): IBinder? {
//        return null
//    }
//
//    companion object {
//        const val CHANNEL_ID = "MediaPlaybackChannel"
//        const val NOTIFICATION_ID = 1
//    }
//}

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
                    println("============================= play end $currentItemIndex")
                    if (musicPlayerState.isPlaying) {
                        next()
                    }
                }

                Player.STATE_READY -> {
                    println("============================= play read $currentItemIndex")

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

        println("============================= start $index")

        if (index >= mediaItems.size || index < 0) return
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

        println("============================= next")

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

        println("============================= playWithIndex $index ${mediaItems.size}")

        musicPlayerState.currentIndex = index
        if (index >= 0 && mediaItems.size > index) {
            println("============================= for $index")
            val playItem = mediaItems[index]
            mediaPlayer.setMediaItem(playItem)
            println("============================= to play $playItem")
            mediaPlayer.play()
            println("============================= to play $index")
        }
    }

}