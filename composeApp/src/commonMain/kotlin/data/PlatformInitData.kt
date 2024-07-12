package data

import androidx.compose.ui.window.WindowState


data class PlatformInitData(
    val winState: WindowState = WindowState(),
    val isFullScreen: Boolean = false,
)