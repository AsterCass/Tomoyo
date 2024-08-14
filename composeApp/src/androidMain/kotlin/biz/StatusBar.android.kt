package biz

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

actual class StatusBar actual constructor() {
    @Composable
    actual fun updateColor(
        bgColor: Color,
        textColorIsDark: Boolean
    ) {
        //use enableEdgeToEdge() instead

//        val view = LocalView.current
//        if (!view.isInEditMode) {
//            SideEffect {
//                val window = (view.context as Activity).window
//                window.statusBarColor = bgColor.toArgb()
//                WindowCompat.getInsetsController(window, view)
//                    .isAppearanceLightStatusBars = textColorIsDark
//            }
//        }
    }
}