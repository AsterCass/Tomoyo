import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import constant.enums.WindowsSizeEnum
import data.PlatformInitData
import javafx.embed.swing.JFXPanel
import java.awt.Dimension
import java.awt.GraphicsEnvironment
import java.awt.Toolkit
import java.awt.Window

val jfxPanel = JFXPanel()

fun main() {

    println("这里是开始动画")

//    thread {
    println("加载开始动画")
    // Application.launch(StartLoading::class.java)
//    }
    println("同步加载主应用")

    application {

        println("应用加载完成")

        val state = rememberWindowState(
            placement = WindowPlacement.Floating,
            position = WindowPosition.PlatformDefault,
            size = WindowsSizeEnum.LOW.data,
        )

        Window(
            onCloseRequest = ::exitApplication,
            title = "Tomoyo",
            icon = MyAppIcon,
            state = state,
//            undecorated = true

        ) {

            MainApp(
                getPlatformData = {
                    PlatformInitData(
                        winState = state,
                        isFullScreen = window.size == getScreenSize(window),
                    )
                },
                updatePlatformData = { data ->

                    val maxSize = getScreenSize(window)

                    println("==========================")
                    println("当前窗口大小：${window.size}")
                    println("当前屏幕大小：$maxSize")
                    println("即将调整大小：${data.winState.size}")


                    if (data.winState.size.height.value >= maxSize.height ||
                        data.winState.size.width.value >= maxSize.width
                    ) {
                        println("已到最大值")
                        state.placement = WindowPlacement.Maximized
                    } else {
                        println("还未到最大值")
                        state.size = data.winState.size
                        window.size = Dimension(
                            data.winState.size.width.value.toInt(),
                            data.winState.size.height.value.toInt()
                        )
                    }
                },
            )
            println("窗口加载完成")

        }


    }
}

object MyAppIcon : Painter() {
    override val intrinsicSize = Size(256f, 256f)

    override fun DrawScope.onDraw() {
        drawOval(Color.Green, Offset(size.width / 4, 0f), Size(size.width / 2f, size.height))
        drawOval(Color.Blue, Offset(0f, size.height / 4), Size(size.width, size.height / 2f))
        drawOval(
            Color.Red,
            Offset(size.width / 4, size.height / 4),
            Size(size.width / 2f, size.height / 2f)
        )
    }
}


fun getScreenSize(window: Window): Dimension {
    val graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment()
    val screenDevices = graphicsEnvironment.screenDevices
    for (device in screenDevices) {
        val bounds = device.defaultConfiguration.bounds
        if (bounds.contains(window.location)) {
            return bounds.size
        }
    }
    println("使用了默认屏幕大小")
    return Toolkit.getDefaultToolkit().screenSize
}



