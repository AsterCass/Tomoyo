package data.model

import cafe.adriel.voyager.core.model.ScreenModel
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

}