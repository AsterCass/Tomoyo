package ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
actual fun MediaPlayer(modifier: Modifier, url: String) {

    println("start load MediaPlayer")

    Column {
        Text("Desktop Media TODO")

        //vlc 太大了，暂不处理
//        VideoPlayerImpl(
//            url = url,
//            modifier = Modifier.fillMaxWidth().height(400.dp)
//        )

    }
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

