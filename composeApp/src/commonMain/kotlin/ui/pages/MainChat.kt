package ui.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import data.ChatRowModel
import data.UserDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.sendText
import org.hildan.krossbow.stomp.subscribeText
import org.hildan.krossbow.websocket.sockjs.SockJSClient

private val json = Json {
    prettyPrint = true
    isLenient = true
    ignoreUnknownKeys = true
}

@Composable
fun MainChatScreen(
    userData: UserDataModel = UserDataModel(),
    modifier: Modifier = Modifier
) {

    if (userData.token.isNullOrBlank()) {
        return
    }

    //coroutine
    val sendMsgCoroutine = rememberCoroutineScope()


    var chatRowList by remember { mutableStateOf(emptyList<ChatRowModel>()) }

    val scope = rememberCoroutineScope()

    var msg by rememberSaveable { mutableStateOf("") }

    var chatId by rememberSaveable { mutableStateOf("") }

    var socketSession by remember { mutableStateOf<Any?>(null) }


    LaunchedEffect(Unit) {
        val client = StompClient(SockJSClient())
        val session: StompSession = client.connect(
            "https://api.astercasc.com/yui/chat-websocket/socketAuthNoError?User-Token=${userData.token}"
        )
        socketSession = session
        val subscription: Flow<String> =
            session.subscribeText("/user/${userData.token}/message/receive")
        val collectorJob = scope.launch {
            subscription.collect { msg ->
                val chatRow: ChatRowModel = Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                }.decodeFromString(msg)
                chatId = chatRow.fromChatId
                chatRowList = chatRowList + chatRow
                println(chatRowList)
            }
        }
    }

    Column(
    ) {
        if (!userData.token.isNullOrBlank()) {
            TextField(
                value = msg,
                placeholder = { Text("信息") },
                onValueChange = { msg = it }
            )
            Button(
                onClick = {
                    sendMsgCoroutine.launch {
                        (socketSession as? StompSession)?.sendText(
                            "/socket/message/send",
                            "{\"chatId\": \"$chatId\", \"message\": \"$msg\"}"
                        )
                    }

                }
            ) {
                Text("发送")
            }
        }


        LazyColumn(
            modifier = Modifier.height(300.dp)
//        modifier = Modifier.verticalScroll(),
//        verticalArrangement = Arrangement.SpaceBetween
        ) {
            item {

            }
            items(chatRowList.size) { index ->
                ChatRowComposable(item = chatRowList[index])
            }
        }


    }


}


@Composable
fun ChatRowComposable(item: ChatRowModel) {

    Row {
//        Avatar(icon = ImageIO.read(URL(item.sendUserAvatar)).toPainter())
        Text(
            item.sendMessage,
            modifier = Modifier.padding(5.dp)
        )
    }

}


@Composable
fun Avatar(icon: Painter, modifier: Modifier = Modifier.padding(5.dp), size: Int = 40) {
    Surface(
        modifier = modifier.size(size.dp),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primary,
        contentColor = Color.White
    ) {
        Image(
            painter = icon,
            contentDescription = "Avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray, CircleShape)
        )
    }
}