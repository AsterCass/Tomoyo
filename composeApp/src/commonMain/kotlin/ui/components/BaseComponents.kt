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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
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
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import constant.BaseResText
import constant.enums.CustomColorTheme
import constant.enums.MainNavigationEnum
import data.model.MainScreenModel
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import tomoyo.composeapp.generated.resources.Res
import tomoyo.composeapp.generated.resources.btn_cancel
import tomoyo.composeapp.generated.resources.btn_confirm
import tomoyo.composeapp.generated.resources.chat_copy_success
import tomoyo.composeapp.generated.resources.chat_emoji_upload_large
import tomoyo.composeapp.generated.resources.chat_image_delete_tip
import tomoyo.composeapp.generated.resources.chat_image_upload_large
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
fun MainAppBar() {

    val title = MainNavigationEnum.getEnumByCode(LocalNavigator.currentOrThrow.lastItem.key).title
    val mainModel: MainScreenModel = koinInject()

    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.primary,
    ) {
        Row(
            modifier = Modifier.windowInsetsPadding(TopAppBarDefaults.windowInsets)
                .fillMaxWidth()
                .padding(horizontal = 3.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {

            IconButton(
                onClick = { mainModel.updateCustomTheme(CustomColorTheme.DARK) }
            ) {
                Icon(
                    modifier = Modifier.size(25.dp),
                    imageVector = Icons.Default.Menu,
                    contentDescription = null,
                )
            }

            Text(
                text = stringResource(title),
                style = MaterialTheme.typography.titleLarge
            )

            IconButton(
                onClick = { mainModel.updateCustomTheme(CustomColorTheme.COFFEE) }
            ) {
                Icon(
                    modifier = Modifier.size(25.dp),
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                )
            }

        }
    }
}

@Composable
fun MainAppNavigationBar(
    extraNavigationList: List<MainNavigationEnum> = emptyList(),
) {

    Surface {
        Row(
            modifier = Modifier.navigationBarsPadding().fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 7.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MainNavigationEnum.entries.toTypedArray().forEach { nav ->
                val tabNavigator = LocalNavigator.currentOrThrow
                val isSelected = tabNavigator.lastItem == nav.screen
                if (nav == MainNavigationEnum.HOME || extraNavigationList.contains(nav)) {
                    TextButton(
                        modifier = if (isSelected) Modifier else Modifier.graphicsLayer {
                            alpha = 0.6f
                        },
                        colors = if (isSelected) ButtonDefaults.textButtonColors().copy(
                            contentColor = MaterialTheme.colorScheme.primary,
                        ) else
                            ButtonDefaults.textButtonColors().copy(
                                contentColor = LocalContentColor.current
                            ),
                        shape = RoundedCornerShape(4.dp),
                        onClick = { tabNavigator.replaceAll(nav.screen) }
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = nav.icon,
                                contentDescription = nav.name,
                                modifier = Modifier.size(20.dp),
                            )
                            Text(
                                text = stringResource(nav.title),
                                style = MaterialTheme.typography.labelSmall,
                            )
                        }
                    }
                }
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
    BaseResText.deleteImageProConfirm = stringResource(Res.string.chat_image_delete_tip)
    BaseResText.errorTooLargeUpload20 = stringResource(Res.string.chat_emoji_upload_large)
    BaseResText.errorTooLargeUpload5000 = stringResource(Res.string.chat_image_upload_large)
    BaseResText.underDevelopment = stringResource(Res.string.under_development)
    BaseResText.confirmBtn = stringResource(Res.string.btn_confirm)
    BaseResText.cancelBtn = stringResource(Res.string.btn_cancel)
    BaseResText.copyTip = stringResource(Res.string.chat_copy_success)
    BaseResText.userNoLogin = stringResource(Res.string.notification_user_no_login)
    BaseResText.bgColorList = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary,
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
    color: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.TopStart,
    contentBgColor: Color = MaterialTheme.colorScheme.onPrimary,
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
    color: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    bgColor: Color = MaterialTheme.colorScheme.secondaryContainer,
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
            .padding(start = 10.dp, top = 10.dp, end = 10.dp)
            .clip(RoundedCornerShape(5.dp))
            .height(40.dp).fillMaxWidth().background(bgColor)
            .clickable {
                onClick()
            }
            .padding(horizontal = 10.dp)
            //不再加这个clip的话，动画效果会忽略end的部分的padding
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

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    //不再加这个clip的话，动画效果会忽略部分的padding
                    .clip(RoundedCornerShape(5.dp))
            ) {
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

}


