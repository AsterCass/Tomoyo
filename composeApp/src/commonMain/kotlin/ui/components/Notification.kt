package ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import constant.enums.NotificationType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

expect fun sendAppNotification(title: String, content: String)

expect fun clearAppNotification()

@Composable
expect fun CheckAppNotificationPermission(requestPermission: @Composable (() -> Unit) -> Unit)


data class MainNotification(
    val message: String,
    val type: NotificationType,
    var isExpire: Boolean = false,
)

data class MainDialogAlert(
    val message: String,
    val onDismissed: () -> Unit = {},
    val cancelOperationText: String = "",
    val confirmOperationText: String = "",
    val confirmOperation: () -> Unit = {}
)

object NotificationManager {

    private val _notifications = MutableStateFlow<MainNotification?>(null)
    val notifications: StateFlow<MainNotification?> = _notifications
    private val _dialogAlert = MutableStateFlow<MainDialogAlert?>(null)
    val dialogAlert: StateFlow<MainDialogAlert?> = _dialogAlert


    fun showNotification(notification: MainNotification) {
        _notifications.value = notification
    }

    fun clearNotification() {
        _notifications.value = _notifications.value?.copy(isExpire = true)
    }

    fun createDialogAlert(dialogAlert: MainDialogAlert) {
        _dialogAlert.value = dialogAlert
    }

    fun removeDialogAlert() {
        _dialogAlert.value = null
    }
}


@Composable
fun NotificationComponent() {

    val notification by NotificationManager.notifications.collectAsState()

    AnimatedVisibility(
        visible = null != notification && false == notification?.isExpire,
        enter = fadeIn(animationSpec = tween(durationMillis = 1000)) +
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(durationMillis = 1000)
                ),
        exit = fadeOut(animationSpec = tween(durationMillis = 1500)) +
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(durationMillis = 1500)
                ),
    ) {
        Box(
            modifier = Modifier
                .navigationBarsPadding()
                .wrapContentWidth()
                .padding(bottom = 20.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
        ) {
            Text(
                text = notification?.message ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        LaunchedEffect(notification) {
            delay(3000)
            NotificationManager.clearNotification()
        }
    }


    val dialogAlert by NotificationManager.dialogAlert.collectAsState()
    if (null != dialogAlert) {
        MainAlertDialog(
            message = dialogAlert!!.message,
            onDismissed = {
                dialogAlert!!.onDismissed
                NotificationManager.removeDialogAlert()
            },
            cancelOperationText = dialogAlert!!.cancelOperationText,
            confirmOperationText = dialogAlert!!.confirmOperationText,
            confirmOperation = dialogAlert!!.confirmOperation
        )
    }


}