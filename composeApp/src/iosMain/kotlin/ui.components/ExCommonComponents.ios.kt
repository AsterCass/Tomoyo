package ui.components

import androidx.compose.runtime.Composable

@Composable
actual fun FullScreen(content: @Composable () -> Unit) {
    content()
}