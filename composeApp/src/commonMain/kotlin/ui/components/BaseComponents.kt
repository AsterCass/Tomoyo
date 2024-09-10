package ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import api.ApiResText
import api.BaseApi
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.regular.Bell
import constant.BaseResText
import constant.enums.MainNavigationEnum
import constant.enums.NotificationType
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import theme.third
import theme.unselectedColor
import tomoyo.composeapp.generated.resources.Res
import tomoyo.composeapp.generated.resources.login_passwd_error
import tomoyo.composeapp.generated.resources.login_success
import tomoyo.composeapp.generated.resources.nezuko
import tomoyo.composeapp.generated.resources.notification_user_no_login
import tomoyo.composeapp.generated.resources.service_error
import tomoyo.composeapp.generated.resources.under_development

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppBar(

) {

    val title = LocalTabNavigator.current.current.options.title

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        title = {
            Row(
                modifier = Modifier.fillMaxSize().padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(Res.drawable.nezuko),
                    contentDescription = null,
                    modifier = Modifier
                        .size(35.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .border(
                            border = BorderStroke(2.dp, MaterialTheme.colorScheme.onBackground),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable {
                            sendAppNotification("1234", "456")
//                            NotificationManager.showNotification(
//                                MainNotification(
//                                    BaseResText.underDevelopment,
//                                    NotificationType.SUCCESS
//                                )
//                            )
                        }
                )

                Text(title)

                Icon(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .clickable {
                            NotificationManager.showNotification(
                                MainNotification(
                                    BaseResText.underDevelopment,
                                    NotificationType.SUCCESS
                                )
                            )
                        }
                        .size(22.dp),
                    imageVector = FontAwesomeIcons.Regular.Bell,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                )

            }
        },
    )
}

@Composable
fun MainAppNavigationBar(
    extraNavigationList: List<MainNavigationEnum> = emptyList(),
) {

    Row(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface).height(50.dp)
    ) {
        MainNavigationEnum.entries.toTypedArray().forEach { nav ->

            val tabNavigator = LocalTabNavigator.current
            val isSelected = tabNavigator.current == nav.tab

            if (nav == MainNavigationEnum.HOME || extraNavigationList.contains(nav)) {
                NavigationBarItem(
                    modifier = Modifier.padding(0.dp)
                        .clip(RoundedCornerShape(100.dp)),
                    colors = NavigationBarItemDefaults.colors().copy(
                        unselectedIconColor = MaterialTheme.colorScheme.unselectedColor,
                    ),
                    icon = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = nav.icon,
                                contentDescription = nav.name,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                modifier = Modifier.padding(top = 1.dp),
                                text = stringResource(nav.title),
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    },
                    selected = isSelected,
                    onClick = { tabNavigator.current = nav.tab },
                )
            }
        }
    }
}

@Composable
fun MainAlertDialog(
    message: String,
    onDismissed: () -> Unit = {},
    cancelOperationText: String = "",
    confirmOperationText: String = "",
    confirmOperation: () -> Unit = {},
) {
    AlertDialog(
        onDismissRequest = onDismissed,
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            if (confirmOperationText.isNotBlank()) {
                TextButton(onClick = confirmOperation) {
                    Text(text = confirmOperationText)
                }
            }
        },
        dismissButton = {
            if (cancelOperationText.isNotBlank()) {
                TextButton(onClick = onDismissed) {
                    Text(text = cancelOperationText)
                }
            }
        }
    )
}



@Composable
fun InitForNoComposableRes() {
    BaseApi.buildStringRes(
        ApiResText(
            serviceErrorDes = stringResource(Res.string.service_error),
            loginSuccessDes = stringResource(Res.string.login_success),
            loginPasswdErrorDes = stringResource(Res.string.login_passwd_error),
        )
    )
    BaseResText.underDevelopment = stringResource(Res.string.under_development)
    BaseResText.userNoLogin = stringResource(Res.string.notification_user_no_login)
    BaseResText.bgColorList = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.third
    )
}


@Composable
fun MainBaseCardBox(
    color: Color = MaterialTheme.colorScheme.onBackground,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.TopStart,
    contentBgColor: Color = MaterialTheme.colorScheme.background,
    content: @Composable () -> Unit,
) {

    Box(
        modifier = modifier,
    ) {

        val roundSize = 15.dp
        val borderSize = 2.dp
        val bottomBorderSize = 5.dp

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = color,
                    shape = RoundedCornerShape(roundSize)
                )
                .border(
                    border = BorderStroke(borderSize, color),
                    shape = RoundedCornerShape(roundSize)
                )

        ) {
            Box(
                modifier = Modifier
                    .padding(
                        bottom = bottomBorderSize,
                    )
                    .fillMaxSize()
                    .background(
                        color = contentBgColor,
                        shape = RoundedCornerShape(roundSize - borderSize)
                    )
                    .clip(
                        shape = RoundedCornerShape(roundSize)
                    ),
                contentAlignment = alignment,
            ) {
                content()
            }


        }


    }
}


