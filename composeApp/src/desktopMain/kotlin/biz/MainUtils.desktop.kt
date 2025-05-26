package biz

import java.awt.KeyboardFocusManager
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

actual fun copyToClipboard(text: String) {
    val clipboard = Toolkit.getDefaultToolkit().systemClipboard
    val stringSelection = StringSelection(text)
    clipboard.setContents(stringSelection, null)
}

actual fun logInfo(text: String) {
}

actual fun isAppInForeground(): Boolean {
    return KeyboardFocusManager.getCurrentKeyboardFocusManager().activeWindow != null;
}

actual fun afterLogin() {
}

actual fun beforeLogout() {
}

actual fun getPlatform(): String {
    return "desktop"
}

actual fun setUpdateGoogleFirebaseToken(operation: (String) -> Unit) {
}