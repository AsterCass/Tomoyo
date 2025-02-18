package data.model

import api.BaseApi
import cafe.adriel.voyager.core.model.ScreenModel
import data.ChatRowModel
import data.UserChatMsgDto
import data.UserChattingSimple
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ui.components.messageTimeLabelBuilder
import ui.components.newMessageLabel

class ChatScreenModel : ScreenModel {

    private val mutex = Mutex()

    private val _chattingId = MutableStateFlow("")
    fun resetChattingId(id: String = "") {
        _chattingId.value = id
    }

    private val _inputContent = MutableStateFlow(emptyMap<String, String>())
    val inputContent = _inputContent.asStateFlow()
    fun updateInputContent(userId: String, input: String) {
        val newContentMap = _inputContent.value.toMutableMap()
        newContentMap[userId] = input
        _inputContent.value = newContentMap
    }

    private val _currentChatData = MutableStateFlow(UserChattingSimple())
    val currentChatData = _currentChatData.asStateFlow()

    //    private val _chatData = MutableStateFlow(mutableMapOf<String, UserChattingSimple>())
//    val chatData = _chatData.asStateFlow()
    private val _chatDataList = MutableStateFlow<List<UserChattingSimple>>(emptyList())
    val chatDataList = _chatDataList.asStateFlow()
    private fun pushChatData(chatId: String, input: UserChattingSimple) {
//        _chatData.value[chatId] = input
        var alreadyChatting = false
        _chatDataList.value.forEach {
            if (chatId == it.chatId) {
                alreadyChatting = true
                it.userChattingDataFlow.update { currentList ->
                    currentList.toMutableList().apply {
                        this.clear()
                        this.addAll(input.userChattingDataFlow.value)
                    }
                }
                it.lastMessageTime = input.lastMessageTime
                it.lastMessageText = input.lastMessageText
                it.lastMessageId = input.lastMessageId
                it.latestRead = input.latestRead
                it.lastMessageIdFlow.value = input.lastMessageId ?: ""
            }
        }
        if (!alreadyChatting) {
            _chatDataList.update { currentList ->
                currentList.toMutableList().apply {
                    this.add(0, input)
                }
            }
        }
    }

    suspend fun pushChatMessage(token: String, chatRow: ChatRowModel) {
        //data
        val newMessage = UserChatMsgDto(
            sendUserNickname = chatRow.sendUserNickname,
            message = chatRow.sendMessage,
            messageId = chatRow.sendMessageId,
            sendUserAvatar = chatRow.sendUserAvatar,
            sendUserGender = chatRow.sendUserGender,
            sendUserRoleType = chatRow.sendUserRoleType,
            sendDate = chatRow.sendDate,
            sendUserId = chatRow.sendUserId,
        )

        //update map
//        if (_chatData.value.containsKey(chatRow.fromChatId)) {
//            val thisChat = _chatData.value[chatRow.fromChatId]
//            thisChat?.userChattingData?.add(0, newMessage)
//            thisChat?.lastMessageTime = chatRow.sendDate
//            thisChat?.lastMessageId = chatRow.sendMessageId
//            thisChat?.lastMessageText = chatRow.sendMessage
//            thisChat?.latestRead = _currentChatData.value.chatId == chatRow.fromChatId
//            //update status
//            _updateStatus.value++
//        } else {
//            updateChatData(token)
//        }


        //update list
        var lastChatIdIndex = -1

        _chatDataList.update { currentList ->
            currentList.toMutableList().apply {
                this.forEachIndexed { index, it ->
                    if (it.chatId == chatRow.fromChatId) {
                        val lastChatTime = it.userChattingDataFlow.value.getOrNull(0)?.sendDate
                        newMessage.webChatLabel = newMessageLabel(newMessage.sendDate, lastChatTime)
                        it.userChattingDataFlow.update { currentList ->
                            currentList.toMutableList().apply {
                                this.add(0, newMessage)
                            }
                        }
                        it.lastMessageTime = chatRow.sendDate
                        it.lastMessageId = chatRow.sendMessageId
                        it.lastMessageIdFlow.value = chatRow.sendMessageId
                        it.lastMessageText = chatRow.sendMessage
                        it.latestRead = _chattingId.value == it.chatId
                        lastChatIdIndex = index
                    }
                }
            }
        }

        if (lastChatIdIndex >= 0) {
            if (0 != lastChatIdIndex) {
                _chatDataList.update { currentList ->
                    currentList.toMutableList().apply {
                        val element = this.removeAt(lastChatIdIndex)
                        this.add(0, element)
                    }
                }
            }
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
//        _chatData.value = data

        _chatDataList.value = data.values.toMutableList()
    }

    suspend fun loadMoreMessage(token: String) {
        mutex.withLock {
            val chatId = _currentChatData.value.chatId
            val lastMessageId =
                _currentChatData.value.userChattingDataFlow.value.lastOrNull()?.messageId
            if (!_currentChatData.value.clientLoadAllHistoryMessage.value && null != chatId) {
                val moreMessage = BaseApi().moreMessage(token, chatId, lastMessageId ?: "")
                if (moreMessage.isEmpty()) {
                    _currentChatData.value.clientLoadAllHistoryMessage.value = true
                } else {
//                    _chatData.value[chatId]?.userChattingData?.addAll(moreMessage)
                    _chatDataList.update { currentList ->
                        currentList.toMutableList().onEach {
                            if (it.chatId == chatId) {
                                it.userChattingDataFlow.update { currentList ->
                                    currentList.toMutableList().apply {
                                        this.addAll(moreMessage)
                                    }
                                }
                            }
                        }
                    }
                }
                rebuildMessageTime()
            }
        }

    }

    fun clearCurrentChatData(userId: String) {
        if (!_currentChatData.value.chatUserId.isNullOrBlank() && _currentChatData.value.chatUserId != userId) {
            _currentChatData.value = UserChattingSimple()
        }
    }

    fun updateCurrentChatData(chatId: String) {
        _chatDataList.value.forEach {
            if (it.chatId == chatId) {
                _currentChatData.value = it
            }
        }
    }

    suspend fun updateCurrentChatDataWithUserId(token: String, userId: String) {
        if (userId == _currentChatData.value.chatUserId) return
        val data = BaseApi().privateInitChat(token, userId)
        //fix
        data.userChattingDataFlow.value = data.userChattingData.toMutableList()
        data.lastMessageText = data.userChattingData.getOrNull(0)?.message ?: ""
        data.lastMessageId = data.userChattingData.getOrNull(0)?.messageId ?: ""
        data.lastMessageIdFlow.value = data.userChattingData.getOrNull(0)?.messageId ?: ""
        data.latestRead = true
        //assign
        if (!data.chatId.isNullOrBlank()) {
            _currentChatData.value = data
            pushChatData(data.chatId!!, data)
        }
    }

    suspend fun hideChat(token: String, chatId: String) {
//        _chatData.value.remove(chatId)
        _chatDataList.update { currentList ->
            currentList.toMutableList().apply {
                this.removeAll { it.chatId == chatId }
            }
        }
        if (_currentChatData.value.chatId == chatId) {
            _currentChatData.value = UserChattingSimple()
        }
        BaseApi().hideChat(token, chatId)
        updateChatData(token)
    }

    suspend fun readMessage(token: String, chatId: String, messageId: String) {
        BaseApi().readMessage(token, chatId, messageId)
//        _chatData.value[chatId]?.latestRead = "" != messageId
        _chatDataList.value.forEach { obj ->
            if (obj.chatId == chatId) obj.latestRead = "" != messageId
        }
//        updateChatData(token)
    }

    fun rebuildMessageTime() {
        if (_currentChatData.value.userChattingDataFlow.value.isNotEmpty()) {
            messageTimeLabelBuilder(
                _currentChatData.value.userChattingDataFlow.value
            )
        }
    }

    // User emoji pro size cache
    private var _emojiProSizeCache = MutableStateFlow<Map<String, Pair<Float, Int>>>(emptyMap())
    val emojiProSizeCache = _emojiProSizeCache.asStateFlow()
    fun updateEmojiProSizeCache(key: String, value: Pair<Float, Int>) {
        val newCache = _emojiProSizeCache.value.toMutableMap()
        newCache[key] = value
        _emojiProSizeCache.value = newCache.toMap()
    }

}