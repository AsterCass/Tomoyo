package biz

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.PowerManager
import androidx.annotation.OptIn
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import com.aster.yuno.tomoyo.MainActivity
import com.aster.yuno.tomoyo.R
import constant.enums.MusicPlayModel
import data.AudioSimpleModel
import data.MusicPlayerState
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.net.UnknownHostException
import kotlin.random.Random


object PlayerHolder {
    @Volatile
    var player: ExoPlayer? = null

    // APP初始的时候调用

    @OptIn(UnstableApi::class)
    fun init(context: Context) {
        // ExoPlayer（⚠️ 用 Service context）
        val dataSourceFactory =
            DefaultHttpDataSource.Factory().setConnectTimeoutMs(15_000).setReadTimeoutMs(15_000)
                .setAllowCrossProtocolRedirects(true)

        val mediaSourceFactory = DefaultMediaSourceFactory(dataSourceFactory)

        player = ExoPlayer.Builder(context).setMediaSourceFactory(mediaSourceFactory).build()

    }
}

class MediaPlaybackService : Service() {
    private lateinit var wakeLock: PowerManager.WakeLock
    private lateinit var wifiLock: WifiManager.WifiLock

    override fun onCreate() {
        super.onCreate()

        // WakeLock
        val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = pm.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK, "Tomoyo::AudioWakeLock"
        )
        wakeLock.acquire()

        // WifiLock
        val wifi = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiLock = wifi.createWifiLock(
            WifiManager.WIFI_MODE_FULL_HIGH_PERF, "Tomoyo::WifiLock"
        )
        wifiLock.acquire()

        startForeground(NOTIFICATION_ID, buildNotification())
    }

    override fun onDestroy() {

        if (wakeLock.isHeld) wakeLock.release()
        if (wifiLock.isHeld) wifiLock.release()

        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

//    private fun createNotificationChannel() {
//        val name = "Player Service"
//        val descriptionText = "Service for playing media"
//        val importance = NotificationManager.IMPORTANCE_LOW
//        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
//            description = descriptionText
//        }
//        val notificationManager: NotificationManager =
//            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.createNotificationChannel(channel)
//    }

    private fun buildNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Tomoyo")
            .setContentText("Playing music ...")
            .setSmallIcon(R.drawable.logo_pro_background)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    companion object {
        private const val CHANNEL_ID = "PlayerChannel"
        private const val NOTIFICATION_ID = 1
    }
}

actual class AudioPlayer actual constructor(
    private val musicPlayerState: MusicPlayerState,
) : Runnable, KoinComponent {
    private val context: Context by inject()

    private val mediaPlayer: ExoPlayer
        get() = PlayerHolder.player ?: error("MediaPlayer not ready. Service not started.")

    private val handler = Handler(Looper.getMainLooper())

    private val maxPlaySize: Int = 50;

    private val playedItems = mutableListOf<AudioSimpleModel>()

    private val mediaItems = mutableMapOf<String, AudioSimpleModel>()

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
            if (isPlaying) {
                scheduleUpdate()
            } else {
                stopUpdate()

                // 无播放时停止前台服务
                // context.stopService(Intent(context, MediaPlaybackService::class.java))
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            val cause = error.cause
            when {
                cause is UnknownHostException -> {
                    onNetworkError()
                }

                error.errorCode == PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED -> {
                    onNetworkError()
                }

                error.errorCode == PlaybackException.ERROR_CODE_DECODING_FAILED -> {

                }

                else -> {

                }
            }
        }

    }

    init {
        mediaPlayer.addListener(listener)
        mediaPlayer.prepare()
    }

    actual fun start(id: String) {
        //check
        if (!mediaItems.containsKey(id)) return
        currentItemIndex = mediaItems.keys.indexOf(id)
        playWithIndex(currentItemIndex)
    }

    actual fun play() {
        if (musicPlayerState.isPlaying) return

        val intent = Intent(context, MediaPlaybackService::class.java)
        ContextCompat.startForegroundService(context, intent)

        handler.post {
            mediaPlayer.play()
        }
    }

    actual fun pause() {
        if (!musicPlayerState.isPlaying) return
        mediaPlayer.pause()
    }

    actual fun next() {
        when (musicPlayerState.playModel) {
            MusicPlayModel.ORDER.ordinal -> {
                if (mediaItems.isNotEmpty()) {
                    currentItemIndex = currentItemIndex.plus(1).rem(mediaItems.size)
                }
            }

            MusicPlayModel.RANDOM.ordinal -> {
                if (mediaItems.isNotEmpty()) {
                    currentItemIndex = Random.nextInt(mediaItems.size)
                }
            }

            MusicPlayModel.CIRCULATION.ordinal -> {

            }
            else -> {}
        }
        playWithIndex(currentItemIndex)
    }

    actual fun prev() {
        if (playedItems.isEmpty()) return
        val lastItem = playedItems.removeAt(playedItems.lastIndex)
        currentItemIndex = mediaItems.keys.indexOf(lastItem.id)
        playWithIndex(currentItemIndex, false)
    }

    actual fun seekTo(time: Double) {
        musicPlayerState.currentTime = time
        if (musicPlayerState.totalDuration - musicPlayerState.currentTime < 1) {
            next()
        } else {
            mediaPlayer.seekTo((time * 1000).toLong())
        }
    }

    @OptIn(UnstableApi::class)
    actual fun addSongList(songs: Map<String, AudioSimpleModel>) {
        mediaItems += songs
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

    private fun playWithIndex(index: Int, maintainLast: Boolean = true) {
        if (index >= mediaItems.size || index < 0) return
        //maintain played map
        if (maintainLast) {
            val lastItem = mediaItems[musicPlayerState.currentPlayId]
            if (null != lastItem) {
                playedItems.add(lastItem)
                if (playedItems.size > maxPlaySize) {
                    playedItems.removeFirstOrNull()
                }
            }
        }
        //convert
        val currentItem = mediaItems.entries.toList()[index]
        musicPlayerState.currentPlayId = currentItem.key
        val playUrl = currentItem.value.audioUrl
        //start
        val playItem = MediaItem.fromUri(playUrl)
        mediaPlayer.setMediaItem(playItem)
        mediaPlayer.play()
    }

    private fun onNetworkError() {
        musicPlayerState.isBuffering = true

        // 停止进度刷新
        stopUpdate()

        // 延迟重试（给系统恢复网络的时间）
        handler.postDelayed({
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.prepare()
                mediaPlayer.play()
            }
        }, 3000)
    }

}