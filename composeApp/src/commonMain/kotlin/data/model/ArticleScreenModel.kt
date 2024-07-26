package data.model

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ArticleScreenModel : ScreenModel {


    private val _openBottomSheet = MutableStateFlow(false)
    val openBottomSheet = _openBottomSheet.asStateFlow()
    fun openBottomSheet(value: Boolean) {
        _openBottomSheet.value = value
    }




}