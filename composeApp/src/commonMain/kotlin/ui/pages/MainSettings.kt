package ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.PasswordVisualTransformation
import api.baseJsonConf
import data.model.MainScreenModel
import data.store.DataStorageManager
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import org.koin.compose.koinInject

@Composable
fun MainSettingsScreen(
    mainModel: MainScreenModel = koinInject(),
    dataStorageManager: DataStorageManager = koinInject(),
) {

    println("reload MainSettingsScreen")

    //coroutine
    val settingApiCoroutine = rememberCoroutineScope()

    //data
    val userState = mainModel.userState.collectAsState().value
    val syncUserData = mainModel.syncUserData.collectAsState().value
    val userData = userState.userData

    //save data
    if (syncUserData) {
        mainModel.syncedUserData()
        dataStorageManager.setString(
            DataStorageManager.USER_DATA,
            baseJsonConf.encodeToString(userData)
        )
    }

    var account by rememberSaveable { mutableStateOf("") }
    var passwd by rememberSaveable { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        if (userData.token.isNullOrBlank()) {
            TextField(
                value = account,
                onValueChange = { account = it }
            )
            TextField(
                value = passwd,
                visualTransformation = PasswordVisualTransformation(),
                onValueChange = { passwd = it }
            )
            Button(
                onClick = {
                    settingApiCoroutine.launch {
                        mainModel.login(account, passwd)
                    }
                }
            ) {
                Text("登录")
            }
        }

        if (userData.account.isNullOrBlank()) {
            Text("未登录")
        } else {
            Column {
                Text("已登录${userData.nickName}")
                Button(
                    onClick = {
                        settingApiCoroutine.launch {
                            mainModel.logout()
                        }
                    }
                ) {
                    Text("登出")
                }
            }

        }


    }
}