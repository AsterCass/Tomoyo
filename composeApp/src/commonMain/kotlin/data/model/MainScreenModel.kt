package data.model

import androidx.compose.ui.unit.Constraints
import api.BaseApi
import api.baseJsonConf
import cafe.adriel.voyager.core.model.ScreenModel
import data.ChatRowModel
import data.UserState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.subscribeText
import org.hildan.krossbow.websocket.sockjs.SockJSClient


class MainScreenModel : ScreenModel {

    //screen
    private val _mainPageContainerConstraints = MutableStateFlow(Constraints())
    val mainPageContainerConstraints = _mainPageContainerConstraints.asStateFlow()
    fun updateMainPageContainerConstraints(value: Constraints) {
        _mainPageContainerConstraints.value = value
    }

    //socket
    private val _socketClient = MutableStateFlow(StompClient(SockJSClient()))
    private val _socketSession = MutableStateFlow<StompSession?>(null)
    val socketSession = _socketSession.asStateFlow()

    //user data
    private val _userState = MutableStateFlow(UserState())
    val userState = _userState.asStateFlow()

    suspend fun login(account: String, passwd: String) {
        _userState.value.userData = BaseApi().login(account, passwd)
        _userState.value.token = _userState.value.userData.token ?: ""


        CoroutineScope(Dispatchers.IO).launch {
            _socketSession.value?.disconnect()
            _socketSession.value = _socketClient.value.connect(
                "https://api.astercasc.com/yui/chat-websocket/socketAuthNoError?" +
                        "User-Token=${_userState.value.token}"
            )
            val subscription: Flow<String> = _socketSession.value!!.subscribeText(
                "/user/${_userState.value.token}/message/receive"
            )
            subscription.collect { msg ->
                val chatRow: ChatRowModel = baseJsonConf.decodeFromString(msg)
                _currentChatId.value = chatRow.fromChatId
                _currentChatRowList.value += chatRow
            }
        }
    }

    //chat
    private val _currentChatId = MutableStateFlow("")
    val currentChatId = _currentChatId.asStateFlow()

    private val _currentChatRowList = MutableStateFlow(emptyList<ChatRowModel>())
    val currentChatRowList = _currentChatRowList.asStateFlow()


}