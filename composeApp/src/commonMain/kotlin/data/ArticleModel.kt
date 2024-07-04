package data


data class ArticleListModel(
    val status: Int,
    val message: String?,
    val data: List<ArticleSimpleModel>?,
)

data class ArticleSimpleModel(
    val id: String? = null,
    val authorId: String? = null,
    val authorName: String? = null,
)