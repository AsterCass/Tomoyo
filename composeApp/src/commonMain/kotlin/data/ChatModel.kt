package data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.Serializable


@Serializable
data class UserChatStarEmojiSimple(
    var addressType: Int = 0,
    var id: String = "",
    var readAddress: String = "",
)


@Serializable
data class UserChatStarEmojis(
    var fileEmojis: MutableList<UserChatStarEmojiSimple> = mutableListOf(),
    var total: Int? = 0,
)

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
    var lastMessageText: String? = null,

    var lastMessageIdFlow: MutableStateFlow<String> = MutableStateFlow(""),
    var userChattingDataFlow: MutableStateFlow<List<UserChatMsgDto>> = MutableStateFlow(emptyList()),
    var clientLoadAllHistoryMessage: MutableStateFlow<Boolean> = MutableStateFlow(false),
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
    //web
    var webChatLabel: String? = null,
)

@Serializable
data class UserChatParam(
    val toUserId: String
)





