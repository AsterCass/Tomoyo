package api

import constant.BASE_SERVER_ADDRESS
import data.ArticleListModel
import data.ArticleSimpleModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class BaseApi {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    private fun getUrl(path: String): String = BASE_SERVER_ADDRESS + path

    suspend fun getArticleList(offset: Int = 0, keyword: String = ""): List<ArticleSimpleModel> {
        val body = client.get(getUrl("/kotomi/article/list"))
        {
            url {
                parameters.append("offset", offset.toString())
                parameters.append("keyword", keyword)
            }
        }
            .body<ArticleListModel>()
        return if (body.data.isNullOrEmpty()) emptyList() else body.data
    }

    suspend fun getArticleDetail(id: String): String {
        val response = client.get(getUrl("/kotomi/article/$id/content"))
        return response.bodyAsText()
    }


}