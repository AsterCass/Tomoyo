package ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import biz.logInfo
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import constant.enums.ViewEnum
import data.UserChattingSimple
import data.model.ChatScreenModel
import data.model.GlobalDataModel
import data.model.MainScreenModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import org.hildan.krossbow.stomp.sendText
import org.koin.compose.koinInject
import theme.halfTransSurfaceVariant
import ui.components.MessageCard
import ui.components.UserInput

class UserChatScreen(
    private val inputUserId: String = "",
    private val inputChatId: String = "",
) : Screen {

    override val key: ScreenKey = "${ViewEnum.USER_CHAT.code}$uniqueScreenKey"


    @Composable
    override fun Content() {
        val mainModel: MainScreenModel = koinInject()
        val chatScreenModel: ChatScreenModel = koinInject()
        val globalDataModel: GlobalDataModel = koinInject()
//        val dataStorageManager: DataStorageManager = koinInject()

        //navigation
        val navigator = LocalNavigator.currentOrThrow

        //coroutine
        val chatApiCoroutine = rememberCoroutineScope()

//        //soft keyboard
//        val keyboardController = LocalSoftwareKeyboardController.current
//
//        //data
//        val ime = WindowInsets.ime
//        val constraints = mainModel.mainPageContainerConstraints.collectAsState().value
//        val density = LocalDensity.current
//        val minHeightDp = with(density) { constraints.minHeight.toDp() }
//        val imeHeightDp = with(density) { ime.getBottom(density).toDp() }

        //user data
        val userState = mainModel.userState.collectAsState().value
        val socketConnected = globalDataModel.socketConnected.collectAsState().value
        val token = userState.token
        val thisUserId = userState.userData.id

        logInfo("[op:UserChatScreen] Load userState ${userState.token} socket $socketConnected")

        //finish login
        if (token.isBlank() || thisUserId.isNullOrBlank()) {
            navigator.pop()
            return
        }

        if (inputUserId.isNotEmpty()) {
            chatScreenModel.clearCurrentChatData(inputUserId)
            chatApiCoroutine.launch {
                chatScreenModel.updateCurrentChatDataWithUserId(token, inputUserId)
            }
        } else if (inputChatId.isNotBlank()) {
            chatScreenModel.updateCurrentChatData(inputChatId)
        }

        //chat data
        val chatData = chatScreenModel.currentChatData.collectAsState().value
        val chatId = chatData.chatId ?: return

        chatApiCoroutine.launch {
            chatScreenModel.readMessage(
                token = token,
                chatId = chatId,
                messageId = chatData.lastMessageId ?: ""
            )
        }

        //chat status
        DisposableEffect(Unit) {
            chatScreenModel.rebuildMessageTime()
            chatScreenModel.resetChattingId(chatId)
            onDispose {
                chatScreenModel.resetChattingId()
            }
        }

        Column(
            Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                Modifier.padding(top = 5.dp).height(50.dp).fillMaxWidth()
                    .padding(horizontal = 15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.halfTransSurfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                    contentPadding = PaddingValues(0.dp),
                    onClick = { navigator.pop() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = null,
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(horizontal = 15.dp),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = chatData.chatName ?: "",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                if (!chatData.chatUserId.isNullOrBlank()) {
                    Button(
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.halfTransSurfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                        contentPadding = PaddingValues(0.dp),
                        onClick = {
                            navigator.push(UserDetailScreen(chatData.chatUserId!!))
                        }) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = null,
                        )
                    }
                }
            }

            Box(
                modifier = Modifier.weight(1f).fillMaxSize()
            ) {
                //todo 这里之后可以自定义背景
                BaseUserChatColumn(
                    chatData,
                    thisUserId,
                    chatApiCoroutine,
                    token
                )
            }

            Row {
                BaseUserInput(chatId)
            }


        }
    }

    @Composable
    private fun BaseUserChatColumn(
        chatData: UserChattingSimple,
        thisUserId: String,
        chatApiCoroutine: CoroutineScope,
        token: String
    ) {
        val chatScreenModel: ChatScreenModel = koinInject()
        val chatRowList = chatData.userChattingDataFlow.collectAsState().value
        val loadAllHistoryMessage = chatData.clientLoadAllHistoryMessage.collectAsState().value

        LazyColumn(
            state = rememberLazyListState(),
            modifier = Modifier.fillMaxSize(),
            reverseLayout = true
        )
        {
            items(
                count = chatRowList.size,
                key = { index -> index }
            ) { index ->
                MessageCard(
                    item = chatRowList[index], thisUserId = thisUserId,
                    isLatest = index == 0,
                )
            }
            item {
                if (!loadAllHistoryMessage) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(15.dp)
                        )
                    }

                    chatApiCoroutine.launch {
                        chatScreenModel.loadMoreMessage(token)
                    }
                }
            }
        }
    }

    @Composable
    private fun BaseUserInput(chatId: String) {

        val mainModel: MainScreenModel = koinInject()
        val socketSession = mainModel.socketSession.collectAsState().value
        val chatScreenModel: ChatScreenModel = koinInject()
        val inputContent = chatScreenModel.inputContent.collectAsState().value
        val thisInputContent = inputContent[chatId] ?: ""

        UserInput(
            onMessageSent = { msg ->
                CoroutineScope(Dispatchers.IO).launch(mainModel.socketExceptionHandler) {
                    socketSession?.sendText(
                        "/socket/message/send",
                        "{\"chatId\": \"${chatId}\", " +
                                "\"message\": \"$msg\"}"
                    )
                    chatScreenModel.updateInputContent(chatId, "")
                }
            },
            inputText = thisInputContent,
            updateInput = {
                chatScreenModel.updateInputContent(chatId, it)
            }
        )
    }

}