package ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import api.baseJsonConf
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import constant.enums.CustomColorTheme
import constant.enums.ViewEnum
import data.model.MainScreenModel
import data.store.DataStorageManager
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import tomoyo.composeapp.generated.resources.Res
import tomoyo.composeapp.generated.resources.login_btn
import tomoyo.composeapp.generated.resources.logout_btn
import tomoyo.composeapp.generated.resources.main_theme
import ui.views.UserLoginScreen


object MainSettingsScreen : Screen {

    override val key: ScreenKey = "${ViewEnum.TAB_MAIN_SETTINGS.code}$uniqueScreenKey"

    @Composable
    override fun Content() {
        MainSettingsScreen()
    }

}

@Composable
fun MainSettingsScreen(
    mainModel: MainScreenModel = koinInject(),
    dataStorageManager: DataStorageManager = koinInject(),
) {

    //navigation
    val navigator = LocalNavigator.currentOrThrow

    //coroutine
    val settingApiCoroutine = rememberCoroutineScope()

    //data
    val userState = mainModel.userState.collectAsState().value
    val syncUserData = mainModel.syncUserData.collectAsState().value
    val userData = userState.userData
    val token = userState.token

    //save data
    if (syncUserData) {
        mainModel.syncedUserData()
        dataStorageManager.setString(
            DataStorageManager.USER_DATA,
            baseJsonConf.encodeToString(userData)
        )
    }

    val customTheme = mainModel.customTheme.collectAsState().value


    Surface {
        Column(
            modifier = Modifier.fillMaxSize().padding(20.dp),
        ) {


            if (token.isNotBlank()) {
                Button(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                        .height(50.dp),
                    onClick = {
                        settingApiCoroutine.launch {
                            mainModel.logout()
                        }
                    },
                    colors = ButtonDefaults.buttonColors().copy(

                    )
                ) {
                    Text(
                        text = stringResource(Res.string.logout_btn),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            } else {
                Button(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                        .height(50.dp),
                    onClick = {
                        navigator.parent?.push(UserLoginScreen())
                    },
                    colors = ButtonDefaults.buttonColors().copy(

                    )
                ) {
                    Text(
                        text = stringResource(Res.string.login_btn),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }


            Text(
                modifier = Modifier.padding(top = 20.dp, bottom = 15.dp),
                text = stringResource(Res.string.main_theme),
                style = MaterialTheme.typography.titleLarge,

                )

            CustomColorTheme.entries.forEach { theme ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (theme == customTheme),
                            onClick = {
                                mainModel.updateCustomTheme(theme)
                                dataStorageManager.setString(
                                    DataStorageManager.USER_THEME,
                                    theme.name
                                )
                            },
                            role = Role.RadioButton
                        )
                        .padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = (theme == customTheme),
                        onClick = null
                    )
                    Text(
                        text = theme.name,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }



        }
    }
}