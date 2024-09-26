package ui.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import biz.getLastTime
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.LocalPlatformContext
import com.github.panpf.sketch.PlatformContext
import com.github.panpf.sketch.request.ImageRequest
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Circle
import compose.icons.fontawesomeicons.solid.InfoCircle
import constant.BaseResText
import data.UserChattingSimple
import data.model.ChatScreenModel
import data.model.GlobalDataModel
import data.model.MainScreenModel
import data.model.MusicScreenModel
import data.store.DataStorageManager
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import theme.inverseThird
import theme.onThird
import theme.subTextColor
import theme.third
import tomoyo.composeapp.generated.resources.Res
import tomoyo.composeapp.generated.resources.chat_delete
import tomoyo.composeapp.generated.resources.chat_pin
import tomoyo.composeapp.generated.resources.chat_unread
import tomoyo.composeapp.generated.resources.notification_check_network
import tomoyo.composeapp.generated.resources.notification_no_permission_notification
import tomoyo.composeapp.generated.resources.notification_user_login_suggest
import tomoyo.composeapp.generated.resources.play_audio_playing_home
import tomoyo.composeapp.generated.resources.play_audio_playing_pause
import tomoyo.composeapp.generated.resources.user_no_motto
import ui.components.CheckAppNotificationPermission
import ui.components.MainDialogAlert
import ui.components.MainHomeNotificationBox
import ui.components.NotificationManager
import ui.components.SwipeToRevealCard
import ui.components.SwipeToRevealCardOption
import ui.views.MusicsPlayerScreen
import ui.views.UserChatScreen


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
    val globalDataModel: GlobalDataModel = koinInject()
    val dataStorageManager: DataStorageManager = koinInject()
    val mainModel: MainScreenModel = koinInject()
    val musicScreenModel: MusicScreenModel = koinInject()
    val chatDataModel: ChatScreenModel = koinInject()
    val configBlock: (ImageRequest.Builder.() -> Unit) = koinInject()

    //context for image
    val localPlatformContext = LocalPlatformContext.current

    //coroutine
    val commonApiCoroutine = rememberCoroutineScope()

    //navigation
    mainModel.updateShowNavBar(true)
    val navigator = LocalNavigator.currentOrThrow
    val loadingScreen = mainModel.loadingScreen.collectAsState().value
    if (loadingScreen) return

    //data
    val musicPlayerState = musicScreenModel.playerState.collectAsState().value
    val socketConnected = globalDataModel.socketConnected.collectAsState().value
    val chatData = chatDataModel.chatData.collectAsState().value
    val updateStatus = chatDataModel.updateStatus.collectAsState().value
    val chatDataList = chatData.toList()
    val netStatus = globalDataModel.netStatus.collectAsState().value
    val userState = mainModel.userState.collectAsState().value
    val token = userState.token


    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.TopCenter),
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
                return
                //todo show some dialogs
            }

            LazyColumn {

                item {

                    if (!socketConnected || !netStatus) {
                        MainHomeNotificationBox(
                            text = stringResource(Res.string.notification_check_network),
                            icon = FontAwesomeIcons.Solid.InfoCircle,
                            color = MaterialTheme.colorScheme.third,
                            bgColor = MaterialTheme.colorScheme.inverseThird
                        )
                    }

                    CheckAppNotificationPermission { func ->
                        MainHomeNotificationBox(
                            text = stringResource(Res.string.notification_no_permission_notification),
                            icon = FontAwesomeIcons.Solid.InfoCircle,
                        ) {
                            func()
                        }
                    }

                    if (musicPlayerState.currentPlayId.isNotBlank()) {
                        val currentMusic = musicScreenModel.getCurrentMusicData()
                        MainHomeNotificationBox(
                            icon = FontAwesomeIcons.Solid.InfoCircle,
                            isTranslating = musicPlayerState.isPlaying,
                            text = if (musicPlayerState.isPlaying)
                                stringResource(Res.string.play_audio_playing_home)
                                        + currentMusic.audioName
                            else stringResource(Res.string.play_audio_playing_home)
                                    + currentMusic.audioName +
                                    stringResource(Res.string.play_audio_playing_pause)
                        ) {
                            navigator.push(MusicsPlayerScreen())
                        }
                    }

                }

                items(chatDataList.size) { index ->
                    Column(modifier = Modifier.padding(top = 5.dp)) {
                        SwipeToRevealCard(
                            modifier = Modifier.height(70.dp),
                            optionList = listOf(
                                SwipeToRevealCardOption(
                                    optionText = stringResource(Res.string.chat_pin),
                                    optionOperation = {
                                        NotificationManager.createDialogAlert(
                                            MainDialogAlert(
                                                message = BaseResText.underDevelopment,
                                                cancelOperationText = BaseResText.cancelBtn
                                            )
                                        )
                                    },
                                    optColor = MaterialTheme.colorScheme.onSecondary,
                                    optBgColor = MaterialTheme.colorScheme.secondary,
                                    width = 75.dp,
                                ),
                                SwipeToRevealCardOption(
                                    optionText = stringResource(Res.string.chat_unread),
                                    optionOperation = {
                                        commonApiCoroutine.launch {
                                            chatDataModel.readMessage(
                                                token = token,
                                                chatId =
                                                chatDataList[index].second.chatId ?: "",
                                                messageId = ""
                                            )
                                        }
                                    },
                                    optColor = MaterialTheme.colorScheme.onPrimary,
                                    optBgColor = MaterialTheme.colorScheme.primary,
                                    width = 125.dp,
                                ),
                                SwipeToRevealCardOption(
                                    optionText = stringResource(Res.string.chat_delete),
                                    optionOperation = {
                                        commonApiCoroutine.launch {
                                            chatDataModel.hideChat(
                                                token = token,
                                                chatId = chatDataList[index].second.chatId ?: ""
                                            )
                                        }
                                    },
                                    optColor = MaterialTheme.colorScheme.onThird,
                                    optBgColor = MaterialTheme.colorScheme.third,
                                    width = 75.dp,
                                )
                            ),
                            content = {
                                UserChatListItem(
                                    configBlock = configBlock,
                                    localPlatformContext = localPlatformContext,
                                    item = chatDataList[index].second,
                                    onClick = {
                                        commonApiCoroutine.launch {
                                            chatDataModel.readMessage(
                                                token = token,
                                                chatId =
                                                chatDataList[index].second.chatId ?: "",
                                                messageId =
                                                chatDataList[index].second.lastMessageId ?: ""
                                            )
                                            navigator.push(
                                                UserChatScreen(
                                                    "",
                                                    chatDataList[index].second.chatId ?: ""
                                                )
                                            )
                                        }
                                    },
                                )
                            }
                        )
                    }
                }

            }


        }

    }


}


@Composable
fun UserChatListItem(
    item: UserChattingSimple,
    onClick: (String) -> Unit,
    configBlock: (ImageRequest.Builder.() -> Unit),
    localPlatformContext: PlatformContext,
) {

    val chatId = item.chatId
    if (chatId.isNullOrBlank()) {
        return
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick(chatId)
            }
            .fillMaxHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            request = ImageRequest(
                context = localPlatformContext,
                uri = item.chatAvatar,
                configBlock = configBlock,
            ),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 15.dp)
                .size(55.dp)
                .align(Alignment.CenterVertically)
                .clip(CircleShape)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(start = 10.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            Text(
                text = item.chatName
                    ?: stringResource(Res.string.user_no_motto),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = item.lastMessageText ?: "",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.subTextColor,
            )
        }

        Column(
            modifier = Modifier
                .width(100.dp)
                .fillMaxHeight()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            Text(
                text = getLastTime(item.lastMessageTime),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.subTextColor,
            )
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(12.dp),
                imageVector = FontAwesomeIcons.Solid.Circle,
                contentDescription = null,
                tint = if (true == item.latestRead) Color.Transparent
                else MaterialTheme.colorScheme.third
            )
        }


    }

}



