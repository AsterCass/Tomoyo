package biz

import platform.UIKit.UIPasteboard

actual fun copyToClipboard(text: String) {
    UIPasteboard.generalPasteboard.string = text
}

actual fun logInfo(text: String) {
}

actual fun isAppInForeground(): Boolean {
    return false
}

actual fun afterLogin() {
}

actual fun beforeLogout() {
}

actual fun getPlatform(): String {
    return "mobile_ios"
}

actual fun setUpdateGoogleFirebaseToken(operation: (String) -> Unit) {
}