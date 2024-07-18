package ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import data.PlatformInitData
import data.UserDataModel
import ui.components.PlatformSetting

@Composable
fun MainSettingsScreen(
    userData: UserDataModel = UserDataModel(),
    login: (String, String) -> Unit = { _: String, _: String -> },

    platformData: PlatformInitData = PlatformInitData(),
    updatePlatformData: (PlatformInitData) -> Unit = {},

    modifier: Modifier = Modifier
) {

    println("reload MainSettingsScreen")

    var account by rememberSaveable { mutableStateOf("") }
    var passwd by rememberSaveable { mutableStateOf("") }



    Column(
        modifier = modifier,
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
                    login(account, passwd)
                }
            ) {
                Text("登录")
            }
        }

        Text(
            text = if (userData.account.isNullOrBlank()) "未登录" else "已登录",
        )


        PlatformSetting(
            platformData = platformData,
            updatePlatformData = updatePlatformData,
            modifier = modifier,
        )

    }
}