package data.model

import data.UserDataModel
import data.UserState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GlobalDataModel {

    private val _userState = MutableStateFlow(UserState())
    val userState = _userState.asStateFlow()

    fun clearLocalUserState() {
        _userState.value.userData = UserDataModel()
        _userState.value.token = ""
    }

}