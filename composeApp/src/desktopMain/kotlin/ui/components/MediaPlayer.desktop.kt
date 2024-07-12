package ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import javafx.application.Platform
import javafx.embed.swing.JFXPanel
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.scene.media.Media
import javafx.scene.media.MediaException
import javafx.scene.media.MediaPlayer
import javafx.scene.media.MediaView
import jfxPanel
import java.awt.Component
import javax.swing.BoxLayout

@Composable
actual fun MediaPlayer(modifier: Modifier, url: String) {

    println("start load MediaPlayer")

    val mediaPlayerComponent = remember { initializeMediaPlayerComponent() }
    val mediaPlayer = remember { mediaPlayerComponent.mediaPlayer(url) }
    val factory = remember { { mediaPlayerComponent } }

    Column {
        LaunchedEffect(Unit) {
            println(mediaPlayer?.mediaPlayer?.status)
            mediaPlayer?.mediaPlayer?.play()
        }
        DisposableEffect(Unit) {
            onDispose {
                mediaPlayer?.mediaPlayer?.stop()
            }
        }

        SwingPanel(
            background = Color.Black,
            modifier = Modifier.height(720.dp).width(1080.dp).padding(0.dp),
            factory = factory,
        )
    }
}

private fun initializeMediaPlayerComponent(): Component {
    //base
    val root = StackPane()
    val scene = Scene(root, 1080.0, 720.0, javafx.scene.paint.Color.BLACK)
    //element
    val mediaView = MediaView()
    root.children.add(mediaView)
    //panel
    jfxPanel.layout = BoxLayout(jfxPanel, BoxLayout.Y_AXIS)
    jfxPanel.scene = scene
    Platform.setImplicitExit(false)
    return jfxPanel
}

private fun Component.mediaPlayer(url: String): MediaView? {
    if (this is JFXPanel) {
        val view = this.scene.root.childrenUnmodifiable[0]
        if (view is MediaView) {
            val media = Media(url)
            val mediaPlayer = MediaPlayer(media)
            view.mediaPlayer = mediaPlayer


            mediaPlayer.onError = Runnable {
                val error: MediaException = mediaPlayer.error
                println("Media error occurred: " + error.message)
            }
            media.onError = Runnable {
                val error: MediaException = media.error
                println("Media error event: " + error.message)
            }
            mediaPlayer.onReady = Runnable {
                println("ready")
            }
            mediaPlayer.onPlaying = Runnable {
                println("playing")
            }
            mediaPlayer.onStopped = Runnable {
                println("stop")
            }
            mediaPlayer.onStalled = Runnable {
                println("onStalled")
            }

            return view
        }
    }
    return null
}



//@Composable
//fun VideoPlayerImpl(
//    url: String,
//    modifier: Modifier,
//) {
//    println("start load MediaPlayer1")
//    val mediaPlayerComponent = remember { initializeMediaPlayerComponent() }
//    println("start load MediaPlayer1.5")
//    val mediaPlayer = remember { mediaPlayerComponent.mediaPlayer() }
//    println("start load MediaPlayer2")
//    val factory = remember { { mediaPlayerComponent } }
//    /* OR the following code and using SwingPanel(factory = { factory }, ...) */
//    // val factory by rememberUpdatedState(mediaPlayerComponent)
//    println("start load MediaPlayer3")
//    LaunchedEffect(url) { mediaPlayer.media().play/*OR .start*/(url) }
//    DisposableEffect(Unit) { onDispose(mediaPlayer::release) }
//    println("start load MediaPlayer4")
//    SwingPanel(
//        factory = factory,
//        background = Color.Transparent,
//        modifier = modifier
//    )
//    println("start load MediaPlayer5")
//}
//
//private fun initializeMediaPlayerComponent(): Component {
////    NativeLibrary.addSearchPath("libvlc", "Z:\\workspace\\yuno\\tomoyo\\composeApp\\libs\\common\\vlc");
////    NativeDiscovery().discover()
//    val currentDir = Path.of("").toAbsolutePath().toString() + "\\app\\resources\\vlc"
//    println(currentDir)
//    NativeLibrary.addSearchPath(
//        "libvlc",
//        currentDir
//    )
//    Native.load(LibC::class.java)
//    println("start load MediaPlayer1.2")
//    val data = EmbeddedMediaPlayerComponent()
//    println("start load MediaPlayer1.3")
//    return data
//}
//
//private fun Component.mediaPlayer() = when (this) {
//    is CallbackMediaPlayerComponent -> mediaPlayer()
//    is EmbeddedMediaPlayerComponent -> mediaPlayer()
//    else -> error("mediaPlayer() can only be called on vlcj player components")
//}

