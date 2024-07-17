import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import constant.enums.MainNavigationEnum
import constant.enums.WindowsSizeEnum
import data.PlatformInitData


fun main() {

    application {

        Window(
            onCloseRequest = ::exitApplication,
            title = "Tomoyo",
            icon = MyAppIcon,
            state = WindowState(
                placement = WindowPlacement.Floating,
                position = WindowPosition.PlatformDefault,
                size = WindowsSizeEnum.STD.data,
            )
        ) {
            MainApp(
                platformData = PlatformInitData(
                    extraNavigationList = listOf(
                        MainNavigationEnum.ARTICLES,
                        MainNavigationEnum.CHAT,
                        MainNavigationEnum.SETTING,
                    )
                )
            )
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


