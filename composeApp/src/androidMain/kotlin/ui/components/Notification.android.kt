package ui.components


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.core.app.NotificationCompat
import com.aster.yuno.tomoyo.MainActivity
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
actual fun CheckAppNotificationPermission() {

//    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    val notificationPermissionState = rememberPermissionState(
        android.Manifest.permission.POST_NOTIFICATIONS
    )

    if (notificationPermissionState.status.isGranted) {
        println("is Granted")
    } else {
        println("not Granted")
    }
//    }


}

fun createAppNotificationChannel(context: Context) {
    val channelId = "some_channel_idaa"
    val channelName = "Your Channel Name"
    val channelDescription = "Your Channel Description"
    val importance = NotificationManager.IMPORTANCE_DEFAULT
    val channel = NotificationChannel(channelId, channelName, importance).apply {
        description = channelDescription
    }
    val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}


actual fun sendAppNotification(title: String, content: String) {
    val context = MainActivity.mainContext!!

    val channelId = "some_channel_idaa"
    val notificationId = 12341

    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle(title)
        .setContentText(content)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    notificationManager.notify(notificationId, builder.build())
}

