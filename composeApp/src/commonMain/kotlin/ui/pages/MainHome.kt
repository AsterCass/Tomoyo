package ui.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.InfoCircle
import data.UserDataModel
import data.model.MainScreenModel
import data.store.DataStorageManager
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import tomoyo.composeapp.generated.resources.Res
import tomoyo.composeapp.generated.resources.notification_need_reconnect
import tomoyo.composeapp.generated.resources.notification_no_permission_notification
import tomoyo.composeapp.generated.resources.notification_user_login_suggest
import ui.components.CheckAppNotificationPermission
import ui.components.MainHomeNotificationBox


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

    //data
    val userState = mainModel.userState.collectAsState().value
    val token = userState.token


    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 20.dp),
        ) {

            if (token.isBlank()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(Res.string.notification_user_login_suggest),
                        color = MaterialTheme.colorScheme.inversePrimary,
                    )
                }
            } else {

                CheckAppNotificationPermission { func ->
                    MainHomeNotificationBox(
                        text = stringResource(Res.string.notification_no_permission_notification),
                        icon = FontAwesomeIcons.Solid.InfoCircle,
                    ) {
                        func()
                    }
                }


                if (!socketConnected) {
                    val userDataStringDb =
                        dataStorageManager.getNonFlowString(DataStorageManager.USER_DATA)

                    MainHomeNotificationBox(
                        text = stringResource(Res.string.notification_need_reconnect),
                        icon = FontAwesomeIcons.Solid.InfoCircle,
                    ) {
                        if (userDataStringDb.isNotBlank()) {
                            val userDataDb: UserDataModel =
                                baseJsonConf.decodeFromString(userDataStringDb)
                            if (!userDataDb.token.isNullOrBlank()) {
                                commonApiCoroutine.launch {
                                    //todo 检查网络，否则会清掉用户登录状态
                                    mainModel.login(
                                        dbData = userDataDb
                                    )
                                }
                            }
                        }
                    }

                }
            }


        }

    }


}