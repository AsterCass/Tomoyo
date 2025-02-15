package biz

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

actual class StatusBar actual constructor() {
    @Composable
    actual fun UpdateColor(
        statusBarColor: Color,
        navigationBarColor: Color,
        isLight: Boolean,
    ) {
    }
}