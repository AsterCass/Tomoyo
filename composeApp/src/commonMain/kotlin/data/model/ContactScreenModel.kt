package data.model

import api.BaseApi
import cafe.adriel.voyager.core.model.ScreenModel
import data.PublicUserSimpleModel
import data.UserDetailModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ContactScreenModel : ScreenModel {

    private val _loadAllPublicUser = MutableStateFlow(false)
    val loadAllPublicUser = _loadAllPublicUser.asStateFlow()

    private val _publicUserDataList = MutableStateFlow(emptyList<PublicUserSimpleModel>())
    val publicUserDataList = _publicUserDataList.asStateFlow()
    suspend fun loadPublicUser() {
        if (_publicUserDataList.value.isNotEmpty()) {
            return
        }
        _publicUserDataList.value = BaseApi().getPublicUser()
        _loadAllPublicUser.value = true
    }

    private val _userDetail = MutableStateFlow(UserDetailModel())
    val userDetail = _userDetail.asStateFlow()
    suspend fun updateUserDetail(userId: String) {
        if (_userDetail.value.id == userId) {
            return
        }
        _userDetail.value = BaseApi().getUserDetail(userId)
    }


}