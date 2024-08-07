package data.model

import api.BaseApi
import cafe.adriel.voyager.core.model.ScreenModel
import data.ArticleSimpleModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ArticleScreenModel : ScreenModel {

    private val _articleDataKey = MutableStateFlow("")
    val articleDataKey = _articleDataKey.asStateFlow()
    private val _articleIsLoadAll = MutableStateFlow(false)
    val articleIsLoadAll = _articleIsLoadAll.asStateFlow()

    private val _articleDataList = MutableStateFlow(emptyList<ArticleSimpleModel>())
    val articleDataList = _articleDataList.asStateFlow()
    suspend fun updateArticleList() {
        val newData = BaseApi().getArticleList(
            offset = _articleDataList.value.size,
            keyword = _articleDataKey.value
        )
        _articleIsLoadAll.value = newData.isEmpty()
        _articleDataList.value += newData
    }

    fun clearResetKeyword(keyword: String = "") {
        _articleDataKey.value = keyword
        _articleDataList.value = emptyList()
    }

    private val _readingArticleId = MutableStateFlow("")
    private val _readingArticleMeta = MutableStateFlow(ArticleSimpleModel())
    val readingArticleMeta = _readingArticleMeta.asStateFlow()
    private val _readingArticleData = MutableStateFlow("")
    val readingArticleData = _readingArticleData.asStateFlow()
    fun updateReadingMeta(data: ArticleSimpleModel) {
        _readingArticleMeta.value = data
    }
    suspend fun updateReadingArticleData(articleId: String, token: String) {
        if (_readingArticleId.value == articleId) {
            return
        }
        val content = BaseApi().getArticleDetail(articleId, token)
        if (content.isNotBlank()) {
            _readingArticleData.value = content
            _readingArticleId.value = articleId
        } else {
            _readingArticleId.value = articleId
            _readingArticleData.value = "Not Login"
        }

    }


}