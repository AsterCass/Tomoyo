package data.model

import api.BaseApi
import biz.logInfo
import data.UserChatStarEmojiSimple
import data.UserDataModel
import data.UserExData
import data.UserState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GlobalDataModel {

    //user data
    private val _userState = MutableStateFlow(UserState())
    val userState = _userState.asStateFlow()

    fun clearLocalUserState() {
        logInfo("[op:clearLocalUserState] User data clear")
        _userState.value.userData = UserDataModel()
        _userState.value.token = ""
        clearLocalUserExData()
    }

    private val _userExData = UserExData()
    fun getUserExData(): UserExData {
        return _userExData
    }

    fun resetUserEmoji(data: List<UserChatStarEmojiSimple>) {
        _userExData.emojiProListFlow.update { currentList ->
            currentList.toMutableList().apply {
                this.addAll(data)
            }
        }
    }

    fun clearLocalUserExData() {
        _userExData.emojiProListFlow.update { currentList ->
            currentList.toMutableList().apply {
                this.clear()
            }
        }
    }

    //network
    private val _netStatus = MutableStateFlow(true)
    val netStatus = _netStatus.asStateFlow()

    suspend fun checkNetwork() {
        _netStatus.value = BaseApi().checkNetwork()
    }

    fun resetNetStatus(status: Boolean) {
        _netStatus.value = status
    }

    //socket
    private val _socketConnected = MutableStateFlow(true)
    val socketConnected = _socketConnected.asStateFlow()
    fun resetSocketConnected(status: Boolean) {
        _socketConnected.value = status
    }

}