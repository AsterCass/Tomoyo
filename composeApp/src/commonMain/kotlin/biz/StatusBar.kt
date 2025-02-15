package biz

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

expect class StatusBar() {

    @Composable
    fun UpdateColor(
        statusBarColor: Color,
        navigationBarColor: Color,
        isLight: Boolean,
    )

}