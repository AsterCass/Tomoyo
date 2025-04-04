package biz

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