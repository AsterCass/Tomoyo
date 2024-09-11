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
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.koin.java.KoinJavaComponent.inject
import ui.components.MainNotification
import ui.components.NotificationManager
import java.net.ConnectException
import java.net.UnknownHostException


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

sealed class ApiResponse<out T> {
    data class Success<T>(val body: T) : ApiResponse<T>()
    sealed class Error : ApiResponse<Nothing>() {
        data class HttpError(val code: Int, val message: String) : Error()
        data object NetworkError : Error()
        data object SerializationError : Error()
    }
}

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

        HttpResponseValidator {
            validateResponse { response ->
                //...
            }
            handleResponseExceptionWithRequest { exception, request ->
                when (exception) {
                    is UnknownHostException -> {
                        //...
                    }
                    is ConnectException -> {
                        //...
                    }
                    is ResponseException -> {
                        //...
                    }
                    else -> {
                        //...
                    }
                }
            }
        }

    }

    private fun getUrl(path: String): String = BASE_SERVER_ADDRESS + path


    suspend inline fun <reified T> HttpClient.safeRequest(
        url: String,
        block: HttpRequestBuilder.() -> Unit,
    ): ApiResponse<T> =
        try {
            val response = request(url) { block() }
            ApiResponse.Success(response.body())
        } catch (e: ClientRequestException) {
            ApiResponse.Error.HttpError(e.response.status.value, e.message)
        } catch (e: ServerResponseException) {
            ApiResponse.Error.HttpError(e.response.status.value, e.message)
        } catch (e: IOException) {
            ApiResponse.Error.NetworkError
        } catch (e: SerializationException) {
            ApiResponse.Error.SerializationError
        }

    suspend fun login(account: String, passwd: String): UserDataModel {
        try {
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
        } catch (ex: Exception) {
            return UserDataModel()
        }
    }

    suspend fun logout(token: String) {
        client.safeRequest<String>(getUrl("/yui/user/logout/auth"))
        {
            method = HttpMethod.Post
            header("User-Token", token)
        }
    }

    suspend fun isLogin(token: String): Boolean {
        val body = client.safeRequest<ResultObj<Boolean>>(
            getUrl("/yui/user/isLogin/authNoError")
        )
        {
            header("User-Token", token)
        }
        return if (body is ApiResponse.Success) {
            body.body.data ?: false
        } else {
            false
        }
    }

    suspend fun getArticleList(offset: Int = 0, keyword: String = ""): List<ArticleSimpleModel> {
        //delay(2000)
        val body = client.safeRequest<ResultObj<List<ArticleSimpleModel>>>(
            getUrl("/kotomi/article/list")
        ) {
            method = HttpMethod.Get
            url {
                parameters.append("offset", offset.toString())
                parameters.append("keyword", keyword)
            }
        }

        return if (body is ApiResponse.Success) {
            if (body.body.data.isNullOrEmpty()) emptyList() else body.body.data!!
        } else {
            emptyList()
        }
    }

    suspend fun getArticleDetail(id: String, token: String): String {
        val body = client.safeRequest<ResultObj<String?>>(
            getUrl("/kotomi/article/$id/content/auth")
        ) {
            method = HttpMethod.Get
            header("User-Token", token)
        }
        return if (body is ApiResponse.Success) {
            if (600 == body.body.status) globalDataModel.clearLocalUserState()
            if (body.body.data.isNullOrEmpty()) "" else body.body.data!!
        } else {
            return ""
        }
    }


    suspend fun getAllAudio(): List<AudioSimpleModel> {
        val body = client.safeRequest<List<AudioSimpleModel>?>(
            getUrl("/ushio/video-pro/audios")
        ) {
            method = HttpMethod.Get
        }
        return if (body is ApiResponse.Success) {
            body.body ?: emptyList()
        } else {
            emptyList()
        }
    }

    suspend fun getPublicUser(): List<PublicUserSimpleModel> {
        val body = client.safeRequest<List<PublicUserSimpleModel>?>(
            getUrl("/yui/user/public/users")
        ) {
            method = HttpMethod.Get
        }
        return if (body is ApiResponse.Success) {
            body.body ?: emptyList()
        } else {
            emptyList()
        }
    }

    suspend fun getUserDetail(userId: String): UserDetailModel {
        val body = client.safeRequest<UserDetailModel>(
            getUrl("/yui/user/detail?userId=$userId")
        ) {
            method = HttpMethod.Get
        }
        return if (body is ApiResponse.Success) {
            body.body
        } else {
            UserDetailModel()
        }
    }

    suspend fun getArticleCount(userId: String, type: Int): Int {
        val body = client.safeRequest<ResultObj<Int>>(
            getUrl("/kotomi/article/list/count?articleType=$type&authorId=$userId")
        ) {
            method = HttpMethod.Get
        }
        return if (body is ApiResponse.Success) {
            body.body.data ?: 0
        } else {
            0
        }
    }


}