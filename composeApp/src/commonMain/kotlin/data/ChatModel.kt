package data

import kotlinx.serialization.Serializable


@Serializable
data class UserChattingSimple(
    var chatId: String? = null,
    var chatType: Int? = null,
    var chatName: String? = null,
    var chatAvatar: String? = null,
    var chatUserId: String? = null,
    var chatUserGender: Int? = null,
    var chatUserRoleType: Int? = null,
    var lastMessageTime: String? = null,
    var lastMessageId: String? = null,
    var userChattingData: MutableList<UserChatMsgDto> = mutableListOf(),
    var latestRead: Boolean? = null,
    var clientLoadAllHistoryMessage: Boolean = false,
)


@Serializable
data class UserChatMsgDto(
    var messageId: String? = null,
    var sendUserId: String? = null,
    var sendUserNickname: String? = null,
    var sendUserAvatar: String? = null,
    var sendUserGender: Int? = null,
    var sendUserRoleType: Int? = null,
    var sendDate: String? = null,
    var message: String? = null,
)

@Serializable
data class UserChatParam(
    val toUserId: String
)





