package ui.components

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.scene.media.Media
import javafx.scene.media.MediaException
import javafx.scene.media.MediaPlayer
import javafx.scene.media.MediaView
import javafx.stage.Stage


class StartLoading : Application() {
    override fun start(primaryStage: Stage) {
        val videoUrl = "https://api.astercasc.com/ushio/video/play/VC1648909883875288/output.mp4"


        // Create a Media object for the video
        val media = Media(videoUrl)


        // Create a MediaPlayer to control the playback of the media
        val mediaPlayer = MediaPlayer(media)


        // Handle media errors
        mediaPlayer.onError = Runnable {
            val error: MediaException = mediaPlayer.error
            println("Media error occurred: " + error.message)
        }

        // Handle media view errors
        media.onError = Runnable {
            val error: MediaException = media.error
            println("Media error event: " + error.message)
        }


        // Create a MediaView to display the video
        val mediaView = MediaView(mediaPlayer)


        // Add the MediaView to a layout pane
        val root = StackPane()
        root.children.add(mediaView)


        // Create and set up the scene
        val scene = Scene(root, 800.0, 600.0)
        primaryStage.setScene(scene)
        primaryStage.setTitle("Web Video Player")
        primaryStage.show()

        // Start playing the video
        mediaPlayer.play()
    }


}



