package api

import com.alibaba.fastjson2.JSON
import constant.BASE_SERVER_ADDRESS
import data.ArticleListModel
import data.ArticleSimpleModel
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

class BaseApi {

    private val client = HttpClient()

    private fun getUrl(path: String): String = BASE_SERVER_ADDRESS + path

    suspend fun getArticleList(): List<ArticleSimpleModel> {
        val body = client.get(getUrl("/kotomi/article/list")).bodyAsText()
        val data = JSON.parseObject(body, ArticleListModel::class.java)
        return if (data.data.isNullOrEmpty()) emptyList() else data.data
    }

    suspend fun getArticleDetail(id: String): String {
        val response = client.get(getUrl("/kotomi/article/$id/content"))
        return response.bodyAsText()
    }


}