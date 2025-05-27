package biz

import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationState
import platform.UIKit.UIPasteboard

actual fun copyToClipboard(text: String) {
    UIPasteboard.generalPasteboard.string = text
}

actual fun logInfo(text: String) {
}

actual fun isAppInForeground(): Boolean {
    val application = UIApplication.sharedApplication

    return when (application.applicationState) {
        UIApplicationState.UIApplicationStateActive -> true
        UIApplicationState.UIApplicationStateInactive -> true
        UIApplicationState.UIApplicationStateBackground -> false
        else -> false
    }
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