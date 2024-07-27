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
import data.model.MainScreenModel
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun MainSettingsScreen(
    mainModel: MainScreenModel = koinInject(),
) {

    println("reload MainSettingsScreen")

    //coroutine
    val settingApiCoroutine = rememberCoroutineScope()

    //data
    val userState = mainModel.userState.collectAsState().value
    val userData = userState.userData

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

        Text(
            text = if (userData.account.isNullOrBlank()) "未登录" else "已登录${userData.nickName}",
        )

    }
}