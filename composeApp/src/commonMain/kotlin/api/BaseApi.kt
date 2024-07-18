package api

import constant.BASE_SERVER_ADDRESS
import data.ArticleListModel
import data.ArticleSimpleModel
import data.LoginParam
import data.ResultObj
import data.UserDataModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
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

    suspend fun login(account: String, passwd: String): UserDataModel {
        val response = client.post(getUrl("/yui/user/login"))
        {
            contentType(ContentType.Application.Json)
            setBody(
                LoginParam(
                    accountMail = account,
                    passwd = passwd,
                )
            )
        }

        val body = response.body<ResultObj<UserDataModel>>()
        body.data?.token = response.headers["User-Token"]

        println(body.data)

        return body.data ?: UserDataModel()
    }

    suspend fun getArticleList(offset: Int = 0, keyword: String = ""): List<ArticleSimpleModel> {
        //delay(2000)
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