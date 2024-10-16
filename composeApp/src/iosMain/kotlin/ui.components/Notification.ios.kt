package ui.components

import androidx.compose.runtime.Composable


actual fun sendAppNotification(title: String, content: String) {

}

@Composable
actual fun CheckAppNotificationPermission(
    requestPermission: @Composable (() -> Unit) -> Unit
) {
}

actual fun clearAppNotification() {

}