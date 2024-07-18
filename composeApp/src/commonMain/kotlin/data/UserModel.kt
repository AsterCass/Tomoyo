package data

import kotlinx.serialization.Serializable

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