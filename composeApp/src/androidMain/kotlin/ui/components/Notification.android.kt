package ui.components


import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import com.aster.yuno.tomoyo.MainActivity
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@SuppressLint("PermissionLaunchedDuringComposition")
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@ExperimentalPermissionsApi
@Composable
actual fun CheckAppNotificationPermission(
    requestPermission: @Composable (() -> Unit) -> Unit
) {

    val context = LocalContext.current

    val notificationPermission = rememberPermissionState(
        permission = Manifest.permission.POST_NOTIFICATIONS
    )

    if (!notificationPermission.status.isGranted) {

        requestPermission {
            //function 1
            //notificationPermission.launchPermissionRequest()

            //function 2
            val intent = Intent().apply {
                action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            }
            context.startActivity(intent)
        }


    }

    //Toast.makeText(LocalContext.current, "Permission Check", Toast.LENGTH_SHORT).show()
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
    val notificationId = 1

    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle(title)
        .setContentText(content)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    notificationManager.notify(notificationId, builder.build())
}

actual fun clearAppNotification() {
    val context = MainActivity.mainContext!!
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.cancelAll()
}