package biz

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ui.components.sendAppNotification

var globalGoogleFirebaseToken = ""

var updateGoogleFirebaseTokenFun: (String) -> Unit = {}

fun reloadGoogleMessageToken() {
    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            updaterToken(task.result)
        } else {
            Log.w("Tomoyo", "Failed to get google message token ", task.exception)
        }
    }
}

private fun updaterToken(token: String) {
    if (token != globalGoogleFirebaseToken) {
        globalGoogleFirebaseToken = token
        updateGoogleFirebaseTokenFun(token)
        Log.i("Tomoyo", "Current google message token: $globalGoogleFirebaseToken")
    }
}


class FMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        updaterToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Client notification
        remoteMessage.notification?.let {
            showNotification(it.title, it.body)
        }

        // Biz logic
        remoteMessage.data.let {
            if (it.isNotEmpty()) {
                processData(it)
            }
        }
    }

    private fun showNotification(title: String?, body: String?) {
        if (title.isNullOrBlank() && body.isNullOrBlank()) {
            sendAppNotification(title!!, body!!)
        }
    }


    private fun processData(data: Map<String, String>) {
    }

}