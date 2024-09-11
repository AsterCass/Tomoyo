package ui.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import api.baseJsonConf
import cafe.adriel.voyager.core.screen.Screen
import data.UserDataModel
import data.model.MainScreenModel
import data.store.DataStorageManager
import kotlinx.coroutines.launch
import org.koin.compose.koinInject


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
    //inject
    val dataStorageManager: DataStorageManager = koinInject()
    val mainModel: MainScreenModel = koinInject()
    val socketConnected = mainModel.socketConnected.collectAsState().value


    //coroutine
    val commonApiCoroutine = rememberCoroutineScope()


    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 20.dp),
        ) {

            Text(
                text = "Socket 连接状态：$socketConnected",
                style = MaterialTheme.typography.bodyMedium
            )

            if (!socketConnected) {
                val userDataStringDb =
                    dataStorageManager.getNonFlowString(DataStorageManager.USER_DATA)
                Button(onClick = {
                    if (userDataStringDb.isNotBlank()) {
                        val userDataDb: UserDataModel =
                            baseJsonConf.decodeFromString(userDataStringDb)
                        if (!userDataDb.token.isNullOrBlank()) {
                            commonApiCoroutine.launch {
                                mainModel.login(
                                    dbData = userDataDb
                                )
                            }
                        }
                    }
                }) {
                    Text("重连")
                }
            }

//            CheckAppNotificationPermission { func ->
//                NotificationManager.createDialogAlert(
//                    MainDialogAlert(
//                        message = "Need Notification Permission",
//                        confirmOperationText = "Confirm",
//                        confirmOperation = {
//                            func()
//                            NotificationManager.removeDialogAlert()
//                        },
//                    )
//                )
//            }

            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(5.dp),
                text = "没想好放什么",
                style = MaterialTheme.typography.bodyMedium
            )

        }

    }


}