package ui.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import ui.components.CheckAppNotificationPermission
import ui.components.MainDialogAlert
import ui.components.NotificationManager


object MainHomeScreen : Screen {

    private fun readResolve(): Any = MainHomeScreen

    @Composable
    override fun Content() {
        MainHomeScreen()
    }

}

@Composable
fun MainHomeScreen(
) {

    println("reload MainHomeScreen")
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 20.dp),
        ) {

            CheckAppNotificationPermission { func ->
                NotificationManager.createDialogAlert(
                    MainDialogAlert(
                        message = "Need Notification Permission",
                        confirmOperationText = "Confirm",
                        confirmOperation = {
                            func()
                            NotificationManager.removeDialogAlert()
                        },
                    )
                )
            }

            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(5.dp),
                text = "没想好放什么",
                style = MaterialTheme.typography.bodyMedium
            )

        }

    }


}