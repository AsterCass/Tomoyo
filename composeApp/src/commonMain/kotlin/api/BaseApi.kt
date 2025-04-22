package api

import biz.logInfo
import constant.BASE_SERVER_ADDRESS
import constant.NETWORK_CHECK_HOST
import constant.enums.NotificationType
import data.ArticleSimpleModel
import data.AudioSimpleModel
import data.LoginParam
import data.PublicUserSimpleModel
import data.ResultObj
import data.UserChatMsgDto
import data.UserChatParam
import data.UserChatStarEmojiSimple
import data.UserChatStarEmojis
import data.UserChattingSimple
import data.UserDataModel
import data.UserDetailModel
import data.UserUploadRetDto
import data.model.GlobalDataModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ui.components.MainNotification
import ui.components.NotificationManager
import kotlin.coroutines.cancellation.CancellationException

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

class BaseApi : KoinComponent {

    private val globalDataModel: GlobalDataModel by inject()

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
                logInfo("[info:HttpResponseValidator:validateResponse] $response")
                //...
            }
            handleResponseExceptionWithRequest { exception, request ->
                logInfo(
                    "[error:HttpResponseValidator:handleResponseExceptionWithRequest]" +
                            " $exception ${request.url}"
                )

                when (exception) {
                    is ServerResponseException -> {
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


    private suspend inline fun <reified T> HttpClient.safeRequest(
        url: String,
        block: HttpRequestBuilder.() -> Unit,
    ): ApiResponse<T> =
        try {
            logInfo("[op:safeRequest] Start request $url")
            val response = request(url) { block() }
            val body: T = response.body()
            globalDataModel.resetNetStatus(true)
            ApiResponse.Success(body)
        } catch (e: ClientRequestException) {
            logInfo("[op:safeRequest:error:ClientRequestException] Request error $e")
            globalDataModel.checkNetwork()
            ApiResponse.Error.HttpError(e.response.status.value, e.message)
        } catch (e: ServerResponseException) {
            logInfo("[op:safeRequest:error:ServerResponseException] Request error $e")
            globalDataModel.checkNetwork()
            ApiResponse.Error.HttpError(e.response.status.value, e.message)
        } catch (e: IOException) {
            logInfo("[op:safeRequest:error:IOException] Request error $e")
            globalDataModel.resetNetStatus(false)
            ApiResponse.Error.NetworkError
        } catch (e: SerializationException) {
            logInfo("[op:safeRequest:error:SerializationException] Request error $e")
            globalDataModel.checkNetwork()
            ApiResponse.Error.SerializationError
        } catch (e: CancellationException) {
            logInfo("[op:safeRequest:error:CancellationException] Request error $e")
            ApiResponse.Error.SerializationError
        } catch (e: Exception) {
            logInfo("[op:safeRequest:error:Exception] Request error $e")
            globalDataModel.checkNetwork()
            ApiResponse.Error.SerializationError
        }


    suspend fun checkNetwork(): Boolean {
        try {
            val response = client.get(NETWORK_CHECK_HOST)
            return response.status.value == 200
        } catch (ex: Exception) {
            return false
        }
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
            } else {
                NotificationManager.showNotification(
                    MainNotification(
                        apiResText.loginPasswdErrorDes,
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
        val body = client.safeRequest<ResultObj<List<PublicUserSimpleModel>?>>(
            getUrl("/yui/user/public/users")
        ) {
            method = HttpMethod.Get
        }
        return if (body is ApiResponse.Success) {
            body.body.data ?: emptyList()
        } else {
            emptyList()
        }
    }

    suspend fun getUserDetail(userId: String): UserDetailModel {
        val body = client.safeRequest<ResultObj<UserDetailModel>>(
            getUrl("/yui/user/detail?userId=$userId")
        ) {
            method = HttpMethod.Get
        }
        return if (body is ApiResponse.Success) {
            body.body.data ?: UserDetailModel()
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

    suspend fun privateInitChat(token: String, toUserId: String): UserChattingSimple {
        val body = client.safeRequest<ResultObj<UserChattingSimple>>(
            getUrl("/yui/user/chat/private/auth")
        ) {
            contentType(ContentType.Application.Json)
            method = HttpMethod.Post
            header("User-Token", token)
            setBody(
                UserChatParam(
                    toUserId = toUserId
                )
            )
        }
        return if (body is ApiResponse.Success) {
            return body.body.data ?: UserChattingSimple()
        } else {
            UserChattingSimple()
        }
    }


    suspend fun moreMessage(
        token: String,
        chatId: String,
        lastMessage: String
    ): List<UserChatMsgDto> {
        val body = client.safeRequest<ResultObj<List<UserChatMsgDto>>>(
            getUrl("/yui/user/chat/message/authNoError?lastMessage=$lastMessage&chatId=$chatId")
        ) {
            contentType(ContentType.Application.Json)
            method = HttpMethod.Get
            header("User-Token", token)
        }
        return if (body is ApiResponse.Success) {
            return body.body.data ?: emptyList()
        } else {
            emptyList()
        }
    }

    suspend fun chattingUsers(token: String): List<UserChattingSimple> {
        val body = client.safeRequest<ResultObj<List<UserChattingSimple>>>(
            getUrl("/yui/user/chat/chattingUsers/authNoError")
        ) {
            method = HttpMethod.Get
            header("User-Token", token)
        }
        return if (body is ApiResponse.Success) {
            return body.body.data ?: emptyList()
        } else {
            emptyList()
        }
    }

    suspend fun getStarEmojis(token: String): List<UserChatStarEmojiSimple> {
        val body = client.safeRequest<ResultObj<UserChatStarEmojis>>(
            getUrl("/yui/user/file/emoji/star/list/auth?pageNo=1&pageSize=100")
        ) {
            method = HttpMethod.Get
            header("User-Token", token)
        }
        return if (body is ApiResponse.Success) {
            return body.body.data?.fileEmojis ?: emptyList()
        } else {
            emptyList()
        }
    }

    suspend fun hideChat(token: String, chatId: String) {
        client.safeRequest<String>(
            getUrl("/yui/user/chat/hide/auth?hideChatId=${chatId}")
        ) {
            method = HttpMethod.Post
            header("User-Token", token)
        }
    }

    suspend fun readMessage(token: String, chatId: String, messageId: String) {
        client.safeRequest<String>(
            getUrl("/yui/user/chat/read/message/auth?chatId=${chatId}&messageId=${messageId}")
        ) {
            method = HttpMethod.Post
            header("User-Token", token)
        }
    }

    suspend fun uploadUserFile(
        token: String,
        fileType: Int,
        fileData: ByteArray,
        fileName: String
    ): String {
        val body = client.safeRequest<ResultObj<UserUploadRetDto>>(
            getUrl("/yui/user/file/upload/auth?fileType=$fileType")
        ) {
            method = HttpMethod.Post
            header("User-Token", token)
            setBody(MultiPartFormDataContent(formData {
                append(
                    key = "file",
                    value = fileData,
                    headers = Headers.build {
                        append(
                            HttpHeaders.ContentType,
                            ContentType.MultiPart.FormData.contentType
                        )
                        append(
                            HttpHeaders.ContentDisposition,
                            "name=\"file\"; filename=\"$fileName\""
                        )
                    }
                )
            }))
        }

        return if (body is ApiResponse.Success) {
            return body.body.data?.readAddress ?: ""
        } else {
            ""
        }
    }


}