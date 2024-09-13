package data.model

import api.BaseApi
import cafe.adriel.voyager.core.model.ScreenModel
import data.ChatRowModel
import data.UserChatMsgDto
import data.UserChattingSimple
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChatScreenModel : ScreenModel {

    private val _inputContent = MutableStateFlow(emptyMap<String, String>())
    val inputContent = _inputContent.asStateFlow()
    fun updateInputContent(userId: String, input: String) {
        val newContentMap = _inputContent.value.toMutableMap()
        newContentMap[userId] = input
        _inputContent.value = newContentMap
    }

    private val _updateStatus = MutableStateFlow(0L)
    val updateStatus = _updateStatus.asStateFlow()
    private val _currentChatData = MutableStateFlow(UserChattingSimple())
    val currentChatData = _currentChatData.asStateFlow()
    private val _chatData = emptyMap<String, UserChattingSimple>().toMutableMap()
    private fun pushChatData(chatId: String, input: UserChattingSimple) {
        _chatData[chatId] = input
    }

    fun pushChatMessage(token: String, chatRow: ChatRowModel) {
        if (_chatData.containsKey(chatRow.fromChatId)) {
            val newMessage = UserChatMsgDto(
                sendUserNickname = chatRow.sendUserNickname,
                message = chatRow.sendMessage,
                messageId = chatRow.sendMessageId
            )
            _chatData[chatRow.fromChatId]?.userChattingData?.add(0, newMessage)
            if (_currentChatData.value.chatId == chatRow.fromChatId) {
                _updateStatus.value++
            }
        } else {
            //todo reference web function baseDataInit(webIsLogin)
        }
    }

    suspend fun loadMoreMessage(token: String) {
        val chatId = _currentChatData.value.chatId
        val lastMessageId = _currentChatData.value.userChattingData.last().messageId
        if (!_currentChatData.value.clientLoadAllHistoryMessage && null != chatId) {
            val moreMessage = BaseApi().moreMessage(token, chatId, lastMessageId ?: "")
            if (moreMessage.isEmpty()) {
                _currentChatData.value.clientLoadAllHistoryMessage = true
            } else {
                _chatData[chatId]?.userChattingData?.addAll(moreMessage)
            }
            _updateStatus.value++
        }
    }

    suspend fun updateCurrentChatDataWithUserId(token: String, userId: String) {
        if (userId == _currentChatData.value.chatUserId) return
        val data = BaseApi().privateInitChat(token, userId)
        if (!data.chatId.isNullOrBlank()) {
            _currentChatData.value = data
            pushChatData(data.chatId!!, data)
        }
    }


}