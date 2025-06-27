package ui.components


import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
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

            //function 3
//            val intent = Intent().apply {
//                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
//                data = Uri.fromParts("package", context.packageName, null)
//            }
            context.startActivity(intent)
        }


    }

    //Toast.makeText(LocalContext.current, "Permission Check", Toast.LENGTH_SHORT).show()
}

fun createAppNotificationChannel(context: Context) {
    val channel = NotificationChannel(
        "TomoyoCommonChannel",
        "Tomoyo Common Notification",
        NotificationManager.IMPORTANCE_HIGH
    ).apply {
        this.description = description
        enableLights(true)
        enableVibration(true)
        setShowBadge(true)
        setBypassDnd(true)
    }

    val notificationManager = context.getSystemService(NotificationManager::class.java)
    notificationManager.createNotificationChannel(channel)
}


actual fun sendAppNotification(title: String, content: String) {
    val context = MainActivity.mainContext!!


    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val pendingIntent = PendingIntent.getActivity(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )


    val builder = NotificationCompat.Builder(context, "TomoyoCommonChannel")
        .setContentTitle(title)
        .setContentText(content)
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .setContentIntent(pendingIntent)

    val notificationManager = context.getSystemService(NotificationManager::class.java)

    notificationManager.notify(0, builder.build())
}

actual fun clearAppNotification() {
    val context = MainActivity.mainContext!!
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.cancelAll()
}