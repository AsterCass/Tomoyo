package data

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.serialization.Serializable

@Serializable
data class UserSocialLinkDto(
    var qq: String? = null,
    var wechat: String? = null,
    var github: String? = null,
)

@Serializable
data class UserCommunityModel(
    var followNum: Int = 0,
    var fansNum: Int = 0,
    var friendNum: Int = 0,
)

@Serializable
data class UserDetailModel(
    var id: String = "",
    var nickName: String = "",
    var gender: Int = 1,
    var roleType: Int = 1,
    var avatar: String = "",
    var motto: String? = null,
    var mail: String = "",
    var birth: String = "",
    var community: UserCommunityModel = UserCommunityModel(),
    var socialLink: UserSocialLinkDto = UserSocialLinkDto()
)

@Serializable
data class UserDataModel(
    var id: String? = null,
    var mail: String? = null,
    var account: String? = null,
    var avatar: String? = null,
    var nickName: String? = null,
    var token: String? = null,
)

@Serializable
data class PublicUserSimpleModel(
    var id: String = "",
    var nickName: String = "",
    var gender: Int = 1,
    var roleType: Int = 1,
    var avatar: String = "",
    var motto: String? = null,
)

@Stable
class UserState {
    var token by mutableStateOf("")
    var userData by mutableStateOf(UserDataModel())
}

@Serializable
data class LoginParam(
    val accountMail: String,
    val passwd: String,
)

@Serializable
data class ChatRowModel(
    val fromChatId: String = "",
    val sendMessageId: String = "",
    val sendUserAvatar: String = "",
    val sendMessage: String = "",
    val sendUserNickname: String = "",
)