import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
                            visible = true
                        }
                    }
                }
            )
        }

        Window(
            onCloseRequest = { visible = false },
            visible = visible,
            title = "Tomoyo",
//            icon = MyAppIcon,
            state = winState,
        ) {
            val showIcon: BufferedImage = koin.get(named("superLowDpiIcon"))
            window.iconImage = showIcon


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

//object MyAppIcon : Painter() {
//    override val intrinsicSize = Size(256f, 256f)
//
//    override fun DrawScope.onDraw() {
//        drawOval(Color.Green, Offset(size.width / 4, 0f), Size(size.width / 2f, size.height))
//        drawOval(Color.Blue, Offset(0f, size.height / 4), Size(size.width, size.height / 2f))
//        drawOval(
//            Color.Red,
//            Offset(size.width / 4, size.height / 4),
//            Size(size.width / 2f, size.height / 2f)
//        )
//    }
//}


