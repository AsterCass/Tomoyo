package data

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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