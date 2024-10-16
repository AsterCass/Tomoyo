package data.model

import api.BaseApi
import data.UserDataModel
import data.UserState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GlobalDataModel {

    //user data
    private val _userState = MutableStateFlow(UserState())
    val userState = _userState.asStateFlow()

    fun clearLocalUserState() {
        _userState.value.userData = UserDataModel()
        _userState.value.token = ""
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