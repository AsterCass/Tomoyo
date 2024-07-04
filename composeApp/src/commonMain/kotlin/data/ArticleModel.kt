package data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ArticleListModel(
    val status: Int,
    val message: String?,
    val data: List<ArticleSimpleModel>?,
)

@Serializable
data class ArticleSimpleModel(
    @SerialName("id")
    val id: String? = null,
    val authorId: String? = null,
    val authorName: String? = null,
)