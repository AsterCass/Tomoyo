package data.model

import api.BaseApi
import cafe.adriel.voyager.core.model.ScreenModel
import data.ArticleSimpleModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ArticleScreenModel : ScreenModel {


    private val _openBottomSheet = MutableStateFlow(false)
    val openBottomSheet = _openBottomSheet.asStateFlow()
    fun openBottomSheet(value: Boolean) {
        _openBottomSheet.value = value
    }

    private val _articleDataList = MutableStateFlow(emptyList<ArticleSimpleModel>())
    val articleDataList = _articleDataList.asStateFlow()
    suspend fun updateArticleList() {
        _articleDataList.value += BaseApi().getArticleList(
            offset = _articleDataList.value.size,
        )
    }


}