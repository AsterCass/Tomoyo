package api

import constant.BASE_SERVER_ADDRESS
import constant.enums.NotificationType
import data.ArticleSimpleModel
import data.AudioSimpleModel
import data.LoginParam
import data.PublicUserSimpleModel
import data.ResultObj
import data.UserDataModel
import data.UserDetailModel
import data.model.GlobalDataModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.java.KoinJavaComponent.inject
import ui.components.MainNotification
import ui.components.NotificationManager


val baseJsonConf = Json {
    prettyPrint = true
    isLenient = true
    ignoreUnknownKeys = true
}

data class ApiResText(
    val serviceErrorDes: String = "",
    val loginSuccessDes: String = "",
    val loginPasswdErrorDes: String = "",
)

private var apiResText: ApiResText = ApiResText()

class BaseApi {

    private val globalDataModel: GlobalDataModel by inject(GlobalDataModel::class.java)

    companion object {
        fun buildStringRes(res: ApiResText) {
            apiResText = res
        }
    }

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(baseJsonConf)
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

        if (200 == body.status) {
            NotificationManager.showNotification(
                MainNotification(
                    apiResText.loginSuccessDes,
                    NotificationType.SUCCESS
                )
            )
        }

        return body.data ?: UserDataModel()
    }

    suspend fun logout(token: String) {
        client.post(getUrl("/yui/user/logout/auth"))
        {
            header("User-Token", token)
        }
    }

    suspend fun isLogin(token: String): Boolean {
        val body = client.get(getUrl("/yui/user/isLogin/authNoError"))
        {
            header("User-Token", token)
        }.body<ResultObj<Boolean>>()
        return body.data ?: false
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
            .body<ResultObj<List<ArticleSimpleModel>>>()
        return if (body.data.isNullOrEmpty()) emptyList() else body.data!!
    }

    suspend fun getArticleDetail(id: String, token: String): String {
        val response = client.get(getUrl("/kotomi/article/$id/content/auth")) {
            header("User-Token", token)
        }
        val body = response.body<ResultObj<String?>>()
        if (600 == body.status) globalDataModel.clearLocalUserState()
        return if (body.data.isNullOrEmpty()) "" else body.data!!
    }


    suspend fun getAllAudio(): List<AudioSimpleModel> {
        val response = client.get(getUrl("/ushio/video-pro/audios"))
        return response.body<List<AudioSimpleModel>?>() ?: emptyList()
    }

    suspend fun getPublicUser(): List<PublicUserSimpleModel> {
        val response = client.get(getUrl("/yui/user/public/users"))
        return response.body<ResultObj<List<PublicUserSimpleModel>>>().data ?: emptyList()
    }

    suspend fun getUserDetail(userId: String): UserDetailModel {
        val response = client.get(getUrl("/yui/user/detail?userId=$userId"))
        return response.body<ResultObj<UserDetailModel>>().data ?: UserDetailModel()
    }

    suspend fun getArticleCount(userId: String, type: Int): Int {
        val response = client.get(
            getUrl(
                "/kotomi/article/list/count?articleType=$type&authorId=$userId"
            )
        )
        return response.body<ResultObj<Int>>().data ?: 0
    }


}