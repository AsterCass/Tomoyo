package ui.components

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import api.ApiResText
import api.BaseApi
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.regular.Bell
import constant.BaseResText
import constant.enums.MainNavigationEnum
import data.model.MainScreenModel
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import theme.third
import theme.unselectedColor
import tomoyo.composeapp.generated.resources.Res
import tomoyo.composeapp.generated.resources.btn_cancel
import tomoyo.composeapp.generated.resources.login_passwd_error
import tomoyo.composeapp.generated.resources.login_success
import tomoyo.composeapp.generated.resources.notification_user_no_login
import tomoyo.composeapp.generated.resources.service_error
import tomoyo.composeapp.generated.resources.under_development
import tomoyo.composeapp.generated.resources.web_fri
import tomoyo.composeapp.generated.resources.web_mon
import tomoyo.composeapp.generated.resources.web_sat
import tomoyo.composeapp.generated.resources.web_sun
import tomoyo.composeapp.generated.resources.web_thu
import tomoyo.composeapp.generated.resources.web_tue
import tomoyo.composeapp.generated.resources.web_wed

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

                Icon(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .clickable {
                            sendAppNotification("Test Title", "Test content some data")
                        }
                        .size(28.dp),
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                )
                Text(title)

                Icon(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .clickable {
                            clearAppNotification()
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
    Dialog(
        onDismissRequest = onDismissed,
    ) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(
                    top = 20.dp, start = 10.dp, end = 10.dp, bottom = 10.dp
                ),
                horizontalAlignment =
                if (cancelOperationText.isNotBlank() || confirmOperationText.isNotBlank())
                    Alignment.Start else Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(start = 5.dp, end = 5.dp, bottom = 10.dp),
                    text = message,
                    style = MaterialTheme.typography.bodyMedium
                )

                Row {
                    if (confirmOperationText.isNotBlank()) {
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = if (cancelOperationText.isNotBlank())
                                Alignment.CenterStart else Alignment.Center
                        ) {
                            TextButton(
                                onClick = confirmOperation,
                                shape = RoundedCornerShape(10.dp),
                            ) {
                                Text(text = confirmOperationText)
                            }
                        }
                    }
                    if (cancelOperationText.isNotBlank()) {
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = if ((confirmOperationText.isNotBlank()))
                                Alignment.CenterEnd else Alignment.Center
                        ) {

                            TextButton(
                                onClick = onDismissed,
                                shape = RoundedCornerShape(10.dp),
                            ) {
                                Text(text = cancelOperationText)
                            }
                        }
                    }

                }

            }
        }
    }
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
    BaseResText.cancelBtn = stringResource(Res.string.btn_cancel)
    BaseResText.userNoLogin = stringResource(Res.string.notification_user_no_login)
    BaseResText.bgColorList = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.third
    )
    BaseResText.weekDayList = listOf(
        stringResource(Res.string.web_sun),
        stringResource(Res.string.web_mon),
        stringResource(Res.string.web_tue),
        stringResource(Res.string.web_wed),
        stringResource(Res.string.web_thu),
        stringResource(Res.string.web_fri),
        stringResource(Res.string.web_sat),
        stringResource(Res.string.web_sun),
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


@Composable
fun MainHomeNotificationBox(
    isTranslating: Boolean = false,
    text: String,
    icon: ImageVector? = null,
    color: Color = MaterialTheme.colorScheme.primary,
    bgColor: Color = MaterialTheme.colorScheme.inversePrimary,
    onClick: () -> Unit = {}
) {


    val mainModel: MainScreenModel = koinInject()
    val constraints = mainModel.mainPageContainerConstraints.collectAsState().value
    val minWidth = constraints.maxWidth.toFloat()
    var initWidth by remember { mutableStateOf(-minWidth) }
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = initWidth,
        targetValue = minWidth,
        animationSpec = if (isTranslating) infiniteRepeatable(
            tween(5000, easing = LinearEasing), RepeatMode.Restart
        ) else InfiniteRepeatableSpec(
            animation = tween(durationMillis = 1),
            repeatMode = RepeatMode.Reverse
        ),
    )

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 5.dp)
            .clip(RoundedCornerShape(5.dp))
            .height(40.dp).fillMaxWidth().background(bgColor)
            .clickable {
                onClick()
            }
            .padding(horizontal = 10.dp)
            //不再加这个clip的话，动画效果会忽略end的部分的padding，原因不明
            .clip(RoundedCornerShape(5.dp)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {


            if (icon != null) {
                Icon(
                    imageVector = icon,
                    modifier = Modifier.padding(end = 5.dp).size(20.dp),
                    tint = color,
                    contentDescription = null,
                )
            }


            Box(modifier = Modifier
                .onGloballyPositioned { layoutCoordinates ->
                    initWidth = -layoutCoordinates.size.width.toFloat()
                }) {

                Text(
                    modifier = Modifier
                        .graphicsLayer {
                            if (isTranslating) {
                                translationX = scale
                            }
                        },
                    text = text,
                    color = color,
                    style = MaterialTheme.typography.bodySmall
                )
            }



        }
    }

}


