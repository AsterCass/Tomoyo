package ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
actual fun MediaPlayer(modifier: Modifier, url: String) {
    Column {
        //vlc 太大了，暂不处理
//        VideoPlayerImpl(
//            url = url,
//            modifier = Modifier.fillMaxWidth().height(400.dp)
//        )

//        val videoPanel = JFXPanel()
//        SwingPanel(
//            factory = {
//
//                println("00000000000000000000000")
//
//                val VFXPanel = JFXPanel()
//                println("00000000000000000000000")
//                val m: Media = Media(url)
//                val player: MediaPlayer = javafx.scene.media.MediaPlayer(m)
//                val viewer = MediaView(player)
//                println("00000000000000000000000")
//                val root = StackPane()
//                val scene = Scene(root)
//
//                println("00000000000000000000000")
//                // center video position
//                val screen: Rectangle2D = Screen.getPrimary().getVisualBounds()
//                viewer.x = 100.0
//                viewer.y = 100.0
//
//                println("00000000000000000000000")
//                // resize video based on screen size
//                val width = viewer.fitWidthProperty()
//                val height = viewer.fitHeightProperty()
//                width.bind(Bindings.selectDouble(viewer.sceneProperty(), "width"))
//                height.bind(Bindings.selectDouble(viewer.sceneProperty(), "height"))
//                viewer.isPreserveRatio = true
//                println("00000000000000000000000")
//
//                // add video to stackpane
//                root.children.add(viewer)
//
//                VFXPanel.scene = scene
//                println("00000000000000000000000")
////                player.play();
//                videoPanel.setLayout(BorderLayout())
//                videoPanel.add(VFXPanel, BorderLayout.CENTER)
//
//                println("11111111111111111")
//                videoPanel
//            },
//            background = Color.Transparent,
//            modifier = modifier
//        )


    }
}


//@Composable
//fun VideoPlayerImpl(
//    url: String,
//    modifier: Modifier,
//) {
//    val mediaPlayerComponent = remember { initializeMediaPlayerComponent() }
//    val mediaPlayer = remember { mediaPlayerComponent.mediaPlayer() }
//
//    val factory = remember { { mediaPlayerComponent } }
//    /* OR the following code and using SwingPanel(factory = { factory }, ...) */
//    // val factory by rememberUpdatedState(mediaPlayerComponent)
//
//    LaunchedEffect(url) { mediaPlayer.media().play/*OR .start*/(url) }
//    DisposableEffect(Unit) { onDispose(mediaPlayer::release) }
//    SwingPanel(
//        factory = factory,
//        background = Color.Transparent,
//        modifier = modifier
//    )
//}
//
//private fun initializeMediaPlayerComponent(): Component {
//    NativeDiscovery().discover()
//    return if (isMacOS()) {
//        CallbackMediaPlayerComponent()
//    } else {
//        EmbeddedMediaPlayerComponent()
//    }
//}
//
//
///**
// * Returns [MediaPlayer] from player components.
// * The method names are the same, but they don't share the same parent/interface.
// * That's why we need this method.
// */
//private fun Component.mediaPlayer() = when (this) {
//    is CallbackMediaPlayerComponent -> mediaPlayer()
//    is EmbeddedMediaPlayerComponent -> mediaPlayer()
//    else -> error("mediaPlayer() can only be called on vlcj player components")
//}
//
//private fun isMacOS(): Boolean {
//    val os = System
//        .getProperty("os.name", "generic")
//        .lowercase(Locale.ENGLISH)
//    return "mac" in os || "darwin" in os
//}