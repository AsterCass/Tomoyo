import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.toPainter
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import constant.enums.MainNavigationEnum
import constant.enums.WindowsSizeEnum
import data.PlatformInitData
import di.KoinInit
import javafx.embed.swing.JFXPanel
import org.koin.core.Koin
import org.koin.core.qualifier.named
import ui.components.clearAppNotification
import java.awt.SystemTray
import java.awt.TrayIcon
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.image.BufferedImage


lateinit var koin: Koin
val tray: SystemTray = SystemTray.getSystemTray()

fun main() {

    koin = KoinInit().init()
    koin.loadModules(
        listOf(),
    )

    //init javafx
    JFXPanel()

    val superLowDpiIcon: BufferedImage = koin.get(named("superLowDpiIcon"))
    val trayIcon = TrayIcon(superLowDpiIcon, "Tomoyo")
    tray.add(trayIcon)

    application {

        var visible by remember { mutableStateOf(true) }
        var windowRef: java.awt.Frame? by remember { mutableStateOf(null) }
        val winState = rememberWindowState(
            placement = WindowPlacement.Floating,
            position = WindowPosition.PlatformDefault,
            size = WindowsSizeEnum.LOW.data,
        )
        trayIcon.apply {
            isImageAutoSize = true
            addMouseListener(
                object : MouseListener {
                    override fun mouseClicked(e: MouseEvent?) {}
                    override fun mousePressed(e: MouseEvent?) {}
                    override fun mouseEntered(e: MouseEvent?) {}
                    override fun mouseExited(e: MouseEvent?) {}
                    override fun mouseReleased(e: MouseEvent?) {
                        val isRight = e?.isPopupTrigger ?: false
                        if (isRight) {
                            exitApplication()
                        } else {
                            windowRef?.let { win ->
                                if (!visible) {
                                    visible = true
                                }
                                if (win.state == java.awt.Frame.ICONIFIED) {
                                    win.state = java.awt.Frame.NORMAL
                                }
                                win.toFront()
                                win.requestFocus()
                                win.isAlwaysOnTop = true
                                win.isAlwaysOnTop = false
                                clearAppNotification()
                            }
                        }
                    }
                }
            )
        }

        Window(
            onCloseRequest = { visible = false },
            visible = visible,
            title = "Tomoyo",
            icon = superLowDpiIcon.toPainter(),
            state = winState,
        ) {
            window.iconImage = superLowDpiIcon

            LaunchedEffect(Unit) {
                windowRef = window
            }

            MainApp(
                platformData = PlatformInitData(
                    extraNavigationList = listOf(
                        MainNavigationEnum.ARTICLES,
                        MainNavigationEnum.Contacts,
                        MainNavigationEnum.MUSICS,
                        MainNavigationEnum.SETTING,
                        )
                )
            )
        }
    }
}

