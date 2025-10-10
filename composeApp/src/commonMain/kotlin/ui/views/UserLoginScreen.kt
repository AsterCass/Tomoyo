package ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import api.baseJsonConf
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.internal.BackHandler
import constant.BaseResText
import constant.enums.NotificationType
import constant.enums.ViewEnum
import data.model.MainScreenModel
import data.store.DataStorageManager
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.koinInject
import tomoyo.composeapp.generated.resources.Res
import tomoyo.composeapp.generated.resources.github
import tomoyo.composeapp.generated.resources.google
import tomoyo.composeapp.generated.resources.login_account_placeholder
import tomoyo.composeapp.generated.resources.login_btn
import tomoyo.composeapp.generated.resources.login_check_privacy_terms
import tomoyo.composeapp.generated.resources.login_check_privacy_terms_prefix
import tomoyo.composeapp.generated.resources.login_create_account
import tomoyo.composeapp.generated.resources.login_create_account_prefix
import tomoyo.composeapp.generated.resources.login_more
import tomoyo.composeapp.generated.resources.login_notification_account
import tomoyo.composeapp.generated.resources.login_notification_check
import tomoyo.composeapp.generated.resources.login_notification_passwd
import tomoyo.composeapp.generated.resources.login_passwd_placeholder
import tomoyo.composeapp.generated.resources.login_subtitle
import tomoyo.composeapp.generated.resources.login_title
import tomoyo.composeapp.generated.resources.reddit
import ui.components.MainBaseCardBox
import ui.components.MainDialogAlert
import ui.components.MainNotification
import ui.components.NotificationManager

class UserLoginScreen : Screen {

    override val key: ScreenKey = "${ViewEnum.USER_LOGIN.code}$uniqueScreenKey"

    @OptIn(ExperimentalMaterial3Api::class, InternalVoyagerApi::class)
    @Composable
    override fun Content() {
        val mainModel: MainScreenModel = koinInject()
        val dataStorageManager: DataStorageManager = koinInject()

        //navigation
        val navigator = LocalNavigator.currentOrThrow

        //coroutine
        val loginApiCoroutine = rememberCoroutineScope()

        //soft keyboard
        val keyboardController = LocalSoftwareKeyboardController.current

        //user data
        val userState = mainModel.userState.collectAsState().value
        val syncUserData = mainModel.syncUserData.collectAsState().value
        val userData = userState.userData
        val token = userState.token

        //input focus
        val focusAccount = remember { FocusRequester() }
        val focusPasswd = remember { FocusRequester() }

        //save data
        if (syncUserData) {
            mainModel.syncedUserData()
            dataStorageManager.setString(
                DataStorageManager.USER_DATA,
                baseJsonConf.encodeToString(userData)
            )
        }

        //finish login
        if (token.isNotBlank()) {
            navigator.pop()
            return
        }

        //override back handle
        BackHandler(enabled = true, onBack = {
            navigator.popUntilRoot()
        })

        var account by rememberSaveable { mutableStateOf("") }
        var passwd by rememberSaveable { mutableStateOf("") }
        var checked by rememberSaveable { mutableStateOf(false) }
        var loginEnable by rememberSaveable { mutableStateOf(true) }
        val checkNotificationText = stringResource(Res.string.login_notification_check)
        val accountNotificationText = stringResource(Res.string.login_notification_account)
        val passwdNotificationText = stringResource(Res.string.login_notification_passwd)


        Surface {
            Column(
                Modifier.windowInsetsPadding(WindowInsets.systemBars).fillMaxSize()
                    .padding(vertical = 4.dp, horizontal = 15.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Box(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 3.dp, vertical = 6.dp),
                ) {

                    IconButton(
                        modifier = Modifier.align(Alignment.CenterStart),
                        onClick = { navigator.popUntilRoot() }
                    ) {
                        Icon(
                            modifier = Modifier.size(25.dp),
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                        )
                    }
                }

                Text(
                    modifier = Modifier.padding(top = 10.dp, start = 10.dp),
                    text = stringResource(Res.string.login_title),
                    style = MaterialTheme.typography.headlineMedium,
                )

                Text(
                    modifier = Modifier.padding(10.dp),
                    text = stringResource(Res.string.login_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                Column(
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp)
                ) {

                    MainBaseCardBox(
                        modifier = Modifier.padding(8.dp).height(62.dp),
                        alignment = Alignment.CenterStart,
                    ) {
                        OutlinedTextField(
                            modifier = Modifier.height(52.dp).focusRequester(focusAccount),
                            value = account,
                            onValueChange = { account = it },
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent,
                            ),
                            textStyle = MaterialTheme.typography.bodyMedium,
                            placeholder = {
                                Text(
                                    text = stringResource(Res.string.login_account_placeholder),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.outlineVariant
                                )
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    focusPasswd.requestFocus()
                                },
                            ),
                            maxLines = 1,
                        )
                    }

                    MainBaseCardBox(
                        modifier = Modifier.padding(8.dp).height(62.dp),
                        alignment = Alignment.CenterStart,
                    ) {
                        OutlinedTextField(
                            modifier = Modifier.height(52.dp).focusRequester(focusPasswd),
                            value = passwd,
                            onValueChange = { passwd = it },
                            visualTransformation = PasswordVisualTransformation(),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent,
                            ),
                            textStyle = MaterialTheme.typography.bodyMedium,
                            placeholder = {
                                Text(
                                    text = stringResource(Res.string.login_passwd_placeholder),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.outlineVariant
                                )
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    keyboardController?.hide()
                                }
                            ),
                            maxLines = 1,
                        )
                    }


                    Row(
                        modifier = Modifier.padding(horizontal = 15.dp, vertical = 15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                            Checkbox(
                                modifier = Modifier.padding(end = 3.dp).scale(.9f),
                                checked = checked,
                                onCheckedChange = { checked = it },
                            )
                        }

                        Text(
                            text = stringResource(Res.string.login_check_privacy_terms_prefix),
                            style = MaterialTheme.typography.bodyMedium,
                        )

                        Text(
                            text = stringResource(Res.string.login_check_privacy_terms),
                            textDecoration = TextDecoration.Underline,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.outline,
                            modifier = Modifier
                                .clip(RoundedCornerShape(5.dp))

                                .clickable {
                                    NotificationManager.createDialogAlert(
                                        MainDialogAlert(
                                            message = BaseResText.underDevelopment,
                                            cancelOperationText = BaseResText.cancelBtn
                                        )
                                    )
                                }
                                .padding(1.dp)
                        )

                    }


                    Button(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth()
                            .height(50.dp),
                        enabled = loginEnable,
                        onClick = {

                            if (account.isBlank()) {
                                NotificationManager.showNotification(
                                    MainNotification(
                                        accountNotificationText,
                                        NotificationType.SUCCESS
                                    )
                                )
                                return@Button
                            }

                            if (passwd.isBlank()) {
                                NotificationManager.showNotification(
                                    MainNotification(
                                        passwdNotificationText,
                                        NotificationType.SUCCESS
                                    )
                                )
                                return@Button
                            }

                            if (!checked) {
                                NotificationManager.showNotification(
                                    MainNotification(
                                        checkNotificationText,
                                        NotificationType.SUCCESS
                                    )
                                )
                                return@Button
                            }

                            loginEnable = false
                            loginApiCoroutine.launch {
                                mainModel.login(account, passwd)
                                loginEnable = true
                            }


                        },
                        shape = RoundedCornerShape(15.dp),
                    ) {
                        Text(
                            text = stringResource(Res.string.login_btn),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }


                    Row(
                        modifier = Modifier.padding(top = 30.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Divider(modifier = Modifier.weight(0.3f))
                        Text(
                            modifier = Modifier.padding(10.dp),
                            text = stringResource(Res.string.login_more),
                            color = MaterialTheme.colorScheme.outline,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        Divider(modifier = Modifier.weight(0.3f))

                    }


                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .padding(vertical = 20.dp, horizontal = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        MainBaseCardBox(
                            modifier = Modifier.height(60.dp).width(90.dp),
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable {
                                        NotificationManager.createDialogAlert(
                                            MainDialogAlert(
                                                message = BaseResText.underDevelopment,
                                                cancelOperationText = BaseResText.cancelBtn
                                            )
                                        )
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = vectorResource(Res.drawable.github),
                                    contentDescription = null,
                                    modifier = Modifier.size(25.dp),
                                    tint = Color.Unspecified
                                )
                            }
                        }

                        MainBaseCardBox(
                            modifier = Modifier.height(60.dp).width(90.dp),
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable {
                                        NotificationManager.createDialogAlert(
                                            MainDialogAlert(
                                                message = BaseResText.underDevelopment,
                                                cancelOperationText = BaseResText.cancelBtn
                                            )
                                        )
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = vectorResource(Res.drawable.reddit),
                                    contentDescription = null,
                                    modifier = Modifier.size(25.dp),
                                    tint = Color.Unspecified
                                )
                            }
                        }

                        MainBaseCardBox(
                            modifier = Modifier.height(60.dp).width(90.dp),
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable {
                                        NotificationManager.createDialogAlert(
                                            MainDialogAlert(
                                                message = BaseResText.underDevelopment,
                                                cancelOperationText = BaseResText.cancelBtn
                                            )
                                        )
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = vectorResource(Res.drawable.google),
                                    contentDescription = null,
                                    modifier = Modifier.size(25.dp),
                                    tint = Color.Unspecified
                                )
                            }
                        }
                    }


                    Row(
                        modifier = Modifier.padding(
                            top = 30.dp,
                            start = 20.dp,
                            end = 20.dp,
                            bottom = 20.dp
                        ).fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(Res.string.login_create_account_prefix),
                            style = MaterialTheme.typography.bodyMedium,
                        )

                        Text(
                            text = stringResource(Res.string.login_create_account),
                            textDecoration = TextDecoration.Underline,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier
                                .clip(RoundedCornerShape(5.dp))
                                .clickable {
                                    NotificationManager.createDialogAlert(
                                        MainDialogAlert(
                                            message = BaseResText.underDevelopment,
                                            cancelOperationText = BaseResText.cancelBtn
                                        )
                                    )
                                }
                                .padding(1.dp)
                        )
                    }
                }


            }
        }
    }
}