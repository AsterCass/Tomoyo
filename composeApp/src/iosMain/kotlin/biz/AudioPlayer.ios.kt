package biz

import constant.enums.MusicPlayModel
import data.AudioSimpleModel
import data.MusicPlayerState
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryPlayback
import platform.AVFAudio.setActive
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerItem
import platform.AVFoundation.AVPlayerItemDidPlayToEndTimeNotification
import platform.AVFoundation.AVPlayerTimeControlStatusPlaying
import platform.AVFoundation.addPeriodicTimeObserverForInterval
import platform.AVFoundation.currentItem
import platform.AVFoundation.duration
import platform.AVFoundation.isPlaybackLikelyToKeepUp
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.AVFoundation.removeTimeObserver
import platform.AVFoundation.replaceCurrentItemWithPlayerItem
import platform.AVFoundation.seekToTime
import platform.AVFoundation.timeControlStatus
import platform.CoreMedia.CMTime
import platform.CoreMedia.CMTimeGetSeconds
import platform.CoreMedia.CMTimeMakeWithSeconds
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.Foundation.NSURL
import platform.darwin.Float64
import platform.darwin.NSEC_PER_SEC
import kotlin.random.Random
import kotlin.time.DurationUnit
import kotlin.time.toDuration

actual class AudioPlayer actual constructor(private val musicPlayerState: MusicPlayerState) {

    private val mediaPlayer : AVPlayer = AVPlayer()

    private lateinit var timeObserver: Any

    private val maxPlaySize: Int = 50;

    private val playedItems = mutableListOf<AudioSimpleModel>()

    private val mediaItems = mutableMapOf<String, AudioSimpleModel>()

    private var currentItemIndex = -1

    init {
        setUpAudioSession()
        musicPlayerState.isPlaying = mediaPlayer.timeControlStatus == AVPlayerTimeControlStatusPlaying
    }

    @OptIn(ExperimentalForeignApi::class)
    private val observer: (CValue<CMTime>) -> Unit =  { time: CValue<CMTime> ->
        musicPlayerState.isBuffering = mediaPlayer.currentItem?.isPlaybackLikelyToKeepUp() != true
        musicPlayerState.isPlaying = mediaPlayer.timeControlStatus == AVPlayerTimeControlStatusPlaying
        val rawTime: Float64 = CMTimeGetSeconds(time)
        val parsedTime = rawTime.toDuration(DurationUnit.SECONDS).inWholeSeconds
        musicPlayerState.currentTime = parsedTime.toDouble()
        if (mediaPlayer.currentItem != null){
            val cmTime = CMTimeGetSeconds(mediaPlayer.currentItem!!.duration)
            musicPlayerState.totalDuration = if (cmTime.isNaN()) 0.0 else
                cmTime.toDuration(DurationUnit.SECONDS).inWholeSeconds.toDouble()
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun startTimeObserver(){
        val interval = CMTimeMakeWithSeconds(1.0, NSEC_PER_SEC.toInt())
        timeObserver = mediaPlayer.addPeriodicTimeObserverForInterval(interval, null, observer)
        NSNotificationCenter.defaultCenter.addObserverForName(
            name = AVPlayerItemDidPlayToEndTimeNotification,
            `object` = mediaPlayer.currentItem,
            queue = NSOperationQueue.mainQueue,
            usingBlock = {
                next()
            }
        )
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun setUpAudioSession() {
        try {
            val audioSession = AVAudioSession.sharedInstance()
            audioSession.setCategory(AVAudioSessionCategoryPlayback, null)
            audioSession.setActive(true, null)
        } catch (e: Exception) {
            println("[op:setUpAudioSession] Error setting up audio session: ${e.message}")
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun stop(){
        if (::timeObserver.isInitialized) mediaPlayer.removeTimeObserver(timeObserver)
        mediaPlayer.pause()
        mediaPlayer.currentItem?.seekToTime(CMTimeMakeWithSeconds(0.0, NSEC_PER_SEC.toInt()))
    }

    actual fun start(id: String) {
        if (!mediaItems.containsKey(id)) return
        currentItemIndex = mediaItems.keys.indexOf(id)
        playWithIndex(currentItemIndex)
    }

    actual fun play() {
        if (musicPlayerState.isPlaying) return
        musicPlayerState.isBuffering = true
        mediaPlayer.play()
        musicPlayerState.isPlaying =false
    }

    actual fun pause() {
        if (!musicPlayerState.isPlaying) return
        mediaPlayer.pause()
        musicPlayerState.isPlaying = false
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
        val lastItem = playedItems.removeLast()
        currentItemIndex = mediaItems.keys.indexOf(lastItem.id)
        playWithIndex(currentItemIndex, false)
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun seekTo(time: Double) {
        musicPlayerState.isBuffering = true
        musicPlayerState.currentTime = time
        if (musicPlayerState.totalDuration - musicPlayerState.currentTime < 1) {
            next()
        } else {
            val cmTime = CMTimeMakeWithSeconds(time, NSEC_PER_SEC.toInt())
            mediaPlayer.currentItem?.seekToTime(time = cmTime, completionHandler = {
                musicPlayerState.isBuffering = false
            })
        }
    }

    actual fun addSongList(songs: Map<String, AudioSimpleModel>) {
        mediaItems += songs
    }

    actual fun clearSongs() {
        mediaItems.clear()
    }

    actual fun cleanUp() {
        stop()
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
        //stop
        stop()
        startTimeObserver()
        musicPlayerState.isBuffering = true
        val playItem = AVPlayerItem(uRL = NSURL.URLWithString(URLString = playUrl)!!)
        mediaPlayer.replaceCurrentItemWithPlayerItem(playItem)
        mediaPlayer.play()
    }


}