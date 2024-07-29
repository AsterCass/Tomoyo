package data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArticleSimpleModel(
    @SerialName("id")
    val id: String? = null,
    val authorId: String? = null,
    val authorName: String? = null,
    val createTime: String? = null,
    val authorAvatar: String? = null,
    val articleTitle: String? = null,
    val articleTagList: List<Long>? = null,
    val articleBrief: String? = null,
    val readNum: Int? = 0,
    val likeNum: Int? = 0,
    val commentNum: Int? = 0,
)