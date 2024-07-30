package data.model

import androidx.compose.ui.unit.Constraints
import api.BaseApi
import api.baseJsonConf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import data.ChatRowModel
import data.UserState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.subscribeText
import org.hildan.krossbow.websocket.sockjs.SockJSClient


class MainScreenModel : ScreenModel {

    //coroutine
    private val _commonCoroutine = screenModelScope

    //navigation
    private val _loadingScreen = MutableStateFlow(false)
    val loadingScreen = _loadingScreen.asStateFlow()
    private val _showNavBar = MutableStateFlow(true)
    val showNavBar = _showNavBar.asStateFlow()
    fun updateShowNavBar(value: Boolean) {
        if (value != _showNavBar.value) {
            _showNavBar.value = value
            _loadingScreen.value = true
            _commonCoroutine.launch {
                delay(500)
                _loadingScreen.value = false
            }
        }
    }

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