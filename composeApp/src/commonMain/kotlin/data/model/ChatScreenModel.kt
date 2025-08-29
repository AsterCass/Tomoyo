package data.model

import api.BaseApi
import api.baseJsonConf
import cafe.adriel.voyager.core.model.ScreenModel
import constant.RECENT_EMOJI_MAX_SIZE
import data.ChatRowModel
import data.UserChatMsgDto
import data.UserChattingSimple
import data.store.DataStorageManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ui.components.messageTimeLabelBuilder
import ui.components.newMessageLabel

class ChatScreenModel(
    private val dataStorageManager: DataStorageManager
) : ScreenModel {

    // Emoji pro
    private val _recentEmojiList = MutableStateFlow(listOf<String>())
    val recentEmojiList = _recentEmojiList.asStateFlow()
    private val _recentKaomojiList = MutableStateFlow(listOf<String>())
    val recentKaomojiList = _recentKaomojiList.asStateFlow()
    private val _recentEmojiProList = MutableStateFlow(listOf<String>())
    val recentEmojiProList = _recentEmojiProList.asStateFlow()

    fun initEmojiList(emoji: String, kaomoji: String, emojiPro: String) {
        if (emoji.isNotBlank()) {
            _recentEmojiList.value = baseJsonConf.decodeFromString(emoji)
        }
        if (kaomoji.isNotBlank()) {
            _recentKaomojiList.value = baseJsonConf.decodeFromString(kaomoji)
        }
        if (emojiPro.isNotBlank()) {
            _recentEmojiProList.value = baseJsonConf.decodeFromString(emojiPro)
        }
    }

    fun addRecentEmojiList(data: String) {
        if (_recentEmojiList.value.isNotEmpty() && data == _recentEmojiList.value.first()) {
            return
        }
        val newList = mutableListOf<String>()
        newList.add(data)
        for (preData in _recentEmojiList.value) {
            if (newList.size >= RECENT_EMOJI_MAX_SIZE) {
                break
            }
            if (preData == data) {
                continue
            }
            newList.add(preData)
        }
        _recentEmojiList.value = newList
        dataStorageManager.setString(
            DataStorageManager.RECENT_EMOJI_LIST,
            baseJsonConf.encodeToString(newList)
        )
    }

    fun addRecentKaomojiList(data: String) {
        if (_recentKaomojiList.value.isNotEmpty() && data == _recentKaomojiList.value.first()) {
            return
        }
        val newList = mutableListOf<String>()
        newList.add(data)
        for (preData in _recentKaomojiList.value) {
            if (newList.size >= RECENT_EMOJI_MAX_SIZE) {
                break
            }
            if (preData == data) {
                continue
            }
            newList.add(preData)
        }
        _recentKaomojiList.value = newList
        dataStorageManager.setString(
            DataStorageManager.RECENT_KAOMOJI_LIST,
            baseJsonConf.encodeToString(newList)
        )
    }

    fun addRecentEmojiProList(data: String) {
        if (_recentEmojiProList.value.isNotEmpty() && data == _recentEmojiProList.value.first()) {
            return
        }
        val newList = mutableListOf<String>()
        newList.add(data)
        for (preData in _recentEmojiProList.value) {
            if (newList.size >= RECENT_EMOJI_MAX_SIZE) {
                break
            }
            if (preData == data) {
                continue
            }
            newList.add(preData)
        }
        _recentEmojiProList.value = newList
        dataStorageManager.setString(
            DataStorageManager.RECENT_EMOJI_PRO_LIST,
            baseJsonConf.encodeToString(newList)
        )
    }


    // Base

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
                it.latestReadWeb.value = input.latestReadWeb.value
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


        //update list
        var lastChatIdIndex = -1;
        var alreadyChatting = false

        _chatDataList.update { currentList ->
            currentList.map { item ->
                if (!alreadyChatting) {
                    ++lastChatIdIndex;
                }
                if (item.chatId == chatRow.fromChatId) {
                    alreadyChatting = true

                    val lastChatTime = item.userChattingDataFlow.value.getOrNull(0)?.sendDate
                    item.userChattingDataFlow.update { chattingList ->
                        chattingList.toMutableList().apply {
                            add(
                                0,
                                newMessage.copy(
                                    webChatLabel = newMessageLabel(
                                        newMessage.sendDate,
                                        lastChatTime
                                    )
                                )
                            )
                        }
                    }
                    item.latestReadWeb.value = _chattingId.value == item.chatId
                    item.copy(
                        lastMessageTime = chatRow.sendDate,
                        lastMessageId = chatRow.sendMessageId,
                        lastMessageText = chatRow.sendMessage,
                    )
                } else {
                    item
                }
            }
        }

        if (alreadyChatting) {
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

        _chatDataList.value.forEach {
            // build currentChatData
            if (currentChatData.value.userChattingDataFlow.value.isNotEmpty()) {
                if (it.chatId == currentChatData.value.chatId) {
                    it.userChattingDataFlow.value =
                        currentChatData.value.userChattingDataFlow.value
                    it.clientLoadAllHistoryMessage.value =
                        currentChatData.value.clientLoadAllHistoryMessage.value
                    _currentChatData.value = it
                }
            }
            // base
            it.latestReadWeb.value = it.latestRead == true
        }
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
        if (currentChatData.value.chatUserId.isNullOrBlank() || currentChatData.value.chatUserId == userId) {
            return
        }
        _currentChatData.value = UserChattingSimple()
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
        data.latestReadWeb.value = true
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

    suspend fun readMessage(
        token: String, chatId: String,
        messageId: String = "",
        autoFoundMessageId: Boolean = false
    ) {
        var msgId = messageId;
        if (autoFoundMessageId) {
            msgId = _chatDataList.value.findLast { it.chatId == chatId }?.lastMessageId ?: ""
        }

        _chatDataList.value.forEach { obj ->
            if (obj.chatId == chatId) obj.latestReadWeb.value = "" != msgId
        }

        BaseApi().readMessage(token, chatId, msgId)
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