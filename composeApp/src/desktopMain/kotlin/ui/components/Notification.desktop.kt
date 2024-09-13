package ui.components

import androidx.compose.runtime.Composable
import koin
import org.koin.core.qualifier.named
import tray
import java.awt.event.ActionListener
import java.awt.image.BufferedImage
import javax.swing.Timer

val timer = Timer(500, object : ActionListener {
    var toggle = true
    val showIcon: BufferedImage = koin.get(named("showIcon"))
    val hideIcon: BufferedImage = koin.get(named("hideIcon"))


    override fun actionPerformed(event: java.awt.event.ActionEvent?) {
        tray.trayIcons[0].image = if (toggle) showIcon else hideIcon
        toggle = !toggle

    }
})

actual fun sendAppNotification(title: String, content: String) {
    timer.start()
}

@Composable
actual fun CheckAppNotificationPermission(
    requestPermission: @Composable (() -> Unit) -> Unit
) {
}

actual fun clearAppNotification() {
    timer.stop()
    val showIcon: BufferedImage = koin.get(named("showIcon"))

    tray.trayIcons[0].image = showIcon
}