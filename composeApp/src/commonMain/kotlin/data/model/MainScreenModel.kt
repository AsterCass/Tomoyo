package data.model

import androidx.compose.ui.unit.Constraints
import api.BaseApi
import api.baseJsonConf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import data.ChatRowModel
import data.UserDataModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSockets
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.instrumentation.KrossbowInstrumentation
import org.hildan.krossbow.stomp.subscribeText
import org.hildan.krossbow.websocket.ktor.KtorWebSocketClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class MainScreenModel : ScreenModel, KoinComponent {

    private val globalDataModel: GlobalDataModel by inject()
    private val chatScreenModel: ChatScreenModel by inject()

    private val _userState = globalDataModel.userState
    val userState = _userState

    // Navigation
    private var _secLastKey = ""
    fun getSecondLastNavKey(): String {
        return _secLastKey;
    }

    fun updateSecondLastNavKey(key: String) {
        _secLastKey = key
    }

    //coroutine
    private val _commonCoroutine = screenModelScope

    //screen
    private val _mainPageContainerConstraints = MutableStateFlow(Constraints())
    val mainPageContainerConstraints = _mainPageContainerConstraints.asStateFlow()
    fun updateMainPageContainerConstraints(value: Constraints) {
        _mainPageContainerConstraints.value = value
    }

    //socket

    private val socketExceptionHandlerWithReconnect = CoroutineExceptionHandler { _, exception ->
        println("Reconnect CoroutineException Caught $exception")
        globalDataModel.resetSocketConnected(false)
        CoroutineScope(Dispatchers.IO).launch {
            delay(3000)
            if (globalDataModel.userState.value.token.isNotBlank()) {
                login(
                    dbData = globalDataModel.userState.value.userData,
                    forceLogin = true
                )
            }
        }
        //  logout()
    }
    val socketExceptionHandler = CoroutineExceptionHandler { _, exception ->
        println("CoroutineException Caught $exception")
        globalDataModel.resetSocketConnected(false)
        //  logout()
    }

    private val _collectorJob = MutableStateFlow<Job?>(null)
    private val _socketClient = MutableStateFlow(StompClient(KtorWebSocketClient(
        HttpClient {
            install(WebSockets)
        }
    )) {
        instrumentation = object : KrossbowInstrumentation {
            override suspend fun onWebSocketClosed(cause: Throwable?) {
                println(cause)
                globalDataModel.resetSocketConnected(false)
            }
        }
    })
    private val _socketSession = MutableStateFlow<StompSession?>(null)
    val socketSession = _socketSession.asStateFlow()


    //user data
    private val _firstTryLinkSocket = MutableStateFlow(true)
    val firstTryLinkSocket = _firstTryLinkSocket.asStateFlow()
    fun triedLinkSocket() {
        _firstTryLinkSocket.value = false
    }

    private val _syncUserData = MutableStateFlow(false)
    val syncUserData = _syncUserData.asStateFlow()
    fun syncedUserData() {
        _syncUserData.value = false
    }

    suspend fun login(
        account: String = "", passwd: String = "",
        dbData: UserDataModel? = null,
        forceLogin: Boolean = false,
    ) {
        if (null == dbData) {
            _userState.value.userData = BaseApi().login(account, passwd)
            val thisToken = _userState.value.userData.token
            if (!thisToken.isNullOrBlank()) {
                _syncUserData.value = true
                chatScreenModel.updateChatData(thisToken)
            }
        } else {
            _userState.value.userData = dbData
        }
        _userState.value.token = _userState.value.userData.token ?: ""

        if (_userState.value.token.isBlank()) return

        if (null != dbData) {
            val isLogin = BaseApi().isLogin(_userState.value.token)
            if (!isLogin) {
                if (globalDataModel.netStatus.value) {
                    globalDataModel.clearLocalUserState()
                } else {
                    globalDataModel.resetSocketConnected(false)
                }
                if (!forceLogin) return
            } else {
                chatScreenModel.updateChatData(_userState.value.token)
            }
        }

        println("[op:login] Socket connect start")
        CoroutineScope(Dispatchers.IO).launch(socketExceptionHandlerWithReconnect) {
            try {
                _collectorJob.value?.cancel()
                _socketSession.value?.disconnect()
                _collectorJob.value = null
                _socketSession.value = null
            } catch (ignore: Exception) {
            }

            println("[op:login] Socket connecting")
            _socketSession.value = _socketClient.value.connect(
                "wss://api.astercasc.com/yui/chat-websocket/no-js/socketAuthNoError?" +
                        "User-Token=${_userState.value.token}"
            )
            println("[op:login] Socket connect successful")
            val subscription: Flow<String> = _socketSession.value!!.subscribeText(
                "/user/${_userState.value.token}/message/receive"
            )
            globalDataModel.resetSocketConnected(true)
            _collectorJob.value = _commonCoroutine.launch(socketExceptionHandlerWithReconnect) {
                subscription.collect { msg ->
                    val chatRow: ChatRowModel = baseJsonConf.decodeFromString(msg)
                    println("[op:login] Socket get message from ${chatRow.fromChatId}")
                    chatScreenModel.pushChatMessage(_userState.value.token, chatRow)
                }
            }

        }
    }

    suspend fun logout() {
        BaseApi().logout(_userState.value.token)
        globalDataModel.clearLocalUserState()
        _syncUserData.value = true
    }


}