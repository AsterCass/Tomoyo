package biz

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ui.components.sendAppNotification


class FMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        saveTokenToServer(token)
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

    private fun saveTokenToServer(token: String) {
    }

    private fun processData(data: Map<String, String>) {
    }

}