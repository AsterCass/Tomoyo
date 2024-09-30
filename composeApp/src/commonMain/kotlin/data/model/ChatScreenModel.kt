package data.model

import api.BaseApi
import cafe.adriel.voyager.core.model.ScreenModel
import data.ChatRowModel
import data.UserChatMsgDto
import data.UserChattingSimple
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.springframework.beans.BeanUtils

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
    private val _chatData = MutableStateFlow(mutableMapOf<String, UserChattingSimple>())
    val chatData = _chatData.asStateFlow()
    private val _chatDataList = MutableStateFlow(mutableListOf<UserChattingSimple>())
    val chatDataList = _chatDataList.asStateFlow()
    private fun pushChatData(chatId: String, input: UserChattingSimple) {
        _chatData.value[chatId] = input
        _chatDataList.value.forEach {
            if (chatId == it.chatId) {
                BeanUtils.copyProperties(input, it)
            }
        }
    }

    suspend fun pushChatMessage(token: String, chatRow: ChatRowModel) {
        if (_chatData.value.containsKey(chatRow.fromChatId)) {
            val newMessage = UserChatMsgDto(
                sendUserNickname = chatRow.sendUserNickname,
                message = chatRow.sendMessage,
                messageId = chatRow.sendMessageId,
            )
            //update map
//            val thisChat = _chatData.value[chatRow.fromChatId]
//            thisChat?.userChattingData?.add(0, newMessage)
//            thisChat?.lastMessageTime = chatRow.sendDate
//            thisChat?.lastMessageId = chatRow.sendMessageId
//            thisChat?.lastMessageText = chatRow.sendMessage
//            thisChat?.latestRead = _currentChatData.value.chatId == chatRow.fromChatId
            //update list
            var lastChatIdIndex = -1
            for (index in _chatDataList.value) {
                _chatDataList.value.forEachIndexed { index, it ->
                    if (it.chatId == chatRow.fromChatId) {
                        it.userChattingData.add(0, newMessage)
                        it.lastMessageTime = chatRow.sendDate
                        it.lastMessageId = chatRow.sendMessageId
                        it.lastMessageText = chatRow.sendMessage
                        it.latestRead = _currentChatData.value.chatId == chatRow.fromChatId
                        lastChatIdIndex = index
                    }
                }
            }
            if (lastChatIdIndex > 0) {
                val element = _chatDataList.value.removeAt(lastChatIdIndex)
                _chatDataList.value.add(0, element)
            }

            //update status
            _updateStatus.value++
        } else {
            updateChatData(token)
        }
    }

    suspend fun updateChatData(token: String) {
        val newChatData = BaseApi().chattingUsers(token)
        if (newChatData.isEmpty()) {
            return
        }
        val data = newChatData
            .associateBy { it.chatId }
            .filterKeys { null != it }
            .mapKeys { it.key!! }
            .toMutableMap()
        _chatData.value = data
        _chatDataList.value = data.values.toMutableList()
    }

    suspend fun loadMoreMessage(token: String) {
        val chatId = _currentChatData.value.chatId
        val lastMessageId = _currentChatData.value.userChattingData.lastOrNull()?.messageId
        if (!_currentChatData.value.clientLoadAllHistoryMessage && null != chatId) {
            val moreMessage = BaseApi().moreMessage(token, chatId, lastMessageId ?: "")
            if (moreMessage.isEmpty()) {
                _currentChatData.value.clientLoadAllHistoryMessage = true
            } else {
                _chatData.value[chatId]?.userChattingData?.addAll(moreMessage)
                _chatDataList.value.forEach {
                    if (it.chatId == chatId) {
                        it.userChattingData.addAll(moreMessage)
                    }
                }
            }
            _updateStatus.value++
        }
    }

    fun updateCurrentChatData(chatId: String) {
        if (_chatData.value.containsKey(chatId)) {
            _currentChatData.value = _chatData.value[chatId] ?: UserChattingSimple()
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

    suspend fun hideChat(token: String, chatId: String) {
        _chatData.value.remove(chatId)
        _chatDataList.value.removeIf { it.chatId == chatId }
        _updateStatus.value++
        BaseApi().hideChat(token, chatId)
        updateChatData(token)
    }

    suspend fun readMessage(token: String, chatId: String, messageId: String) {
        BaseApi().readMessage(token, chatId, messageId)
        _chatData.value[chatId]?.latestRead = "" != messageId
        _chatDataList.value.forEach { obj ->
            if (obj.chatId == chatId) obj.latestRead = "" != messageId
        }
        _updateStatus.value++
//        updateChatData(token)
    }


}