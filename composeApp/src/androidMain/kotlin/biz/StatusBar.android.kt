package biz

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

actual class StatusBar actual constructor() {
    @Composable
    actual fun updateColor(
        bgColor: Color,
        textColorIsDark: Boolean
    ) {
        val view = LocalView.current
        if (!view.isInEditMode) {
            SideEffect {
                val window = (view.context as Activity).window
                window.statusBarColor = bgColor.toArgb()
                WindowCompat.getInsetsController(window, view)
                    .isAppearanceLightStatusBars = textColorIsDark
            }
        }
    }
}