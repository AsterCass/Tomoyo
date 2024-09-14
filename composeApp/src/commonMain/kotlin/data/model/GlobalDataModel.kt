package data.model

import constant.NETWORK_CHECK_HOST
import data.UserDataModel
import data.UserState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.InetAddress

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

    fun checkNetwork() {
        CoroutineScope(Dispatchers.IO).launch {
            _netStatus.value = try {
                val address = InetAddress.getByName(NETWORK_CHECK_HOST)
                !address.equals("")
            } catch (e: Exception) {
                false
            }
        }
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