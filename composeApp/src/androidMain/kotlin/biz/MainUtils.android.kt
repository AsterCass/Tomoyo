package biz

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import com.aster.yuno.tomoyo.MainActivity

actual fun copyToClipboard(text: String) {
    val context = MainActivity.mainContext!!
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText("Tomoyo Copy", text)
    clipboardManager.setPrimaryClip(clipData)
}

actual fun logInfo(text: String) {
    Log.i("Tomoyo", text)
}

@SuppressLint("ServiceCast")
actual fun isAppInForeground(): Boolean {
    val context = MainActivity.mainContext!!
    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val appProcesses = activityManager.runningAppProcesses ?: return false

    val packageName = context.packageName
    for (appProcess in appProcesses) {
        if (appProcess.processName == packageName) {
            return appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
        }
    }
    return false
}