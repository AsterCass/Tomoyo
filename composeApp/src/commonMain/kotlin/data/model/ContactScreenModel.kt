package data.model

import api.BaseApi
import cafe.adriel.voyager.core.model.ScreenModel
import constant.enums.UserDetailTabScreenTabModel
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
        val articleNum = BaseApi().getArticleCount(userId, 1)
        val thoughtNum = BaseApi().getArticleCount(userId, 2)
        val baseData = BaseApi().getUserDetail(userId)
        baseData.articleNum = articleNum
        baseData.thoughtNum = thoughtNum
        _userDetail.value = baseData
    }

    //tab
    private val _userDetailTab = MutableStateFlow(UserDetailTabScreenTabModel.FRIENDS)
    val userDetailTab = _userDetailTab.asStateFlow()
    fun updateUserDetailTab(tab: UserDetailTabScreenTabModel) {
        _userDetailTab.value = tab
    }


}