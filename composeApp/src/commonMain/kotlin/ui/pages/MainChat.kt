package ui.pages

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.UserChatMsgDto
import data.model.MainScreenModel
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import tomoyo.composeapp.generated.resources.Res
import tomoyo.composeapp.generated.resources.chat_send

@Composable
fun MainChatScreen(
    mainModel: MainScreenModel = koinInject(),
) {

    //data
    val userState = mainModel.userState.collectAsState().value
//    val socketSession = mainModel.socketSession.collectAsState().value
//    val chatId = mainModel.currentChatId.collectAsState().value
//    val chatRowList = mainModel.currentChatRowList.collectAsState().value

    val userData = userState.userData


    if (userData.token.isNullOrBlank()) {
        return
    }

    //coroutine
    val sendMsgCoroutine = rememberCoroutineScope()

    var chatMessage by rememberSaveable { mutableStateOf("") }

    Column(
    ) {
        if (!userData.token.isNullOrBlank()) {
            TextField(
                value = chatMessage,
                placeholder = { Text("信息") }, //todo
                onValueChange = { chatMessage = it }
            )
            Button(
                onClick = {
//                    sendMsgCoroutine.launch {
//                    CoroutineScope(Dispatchers.IO).launch {
//                        socketSession?.sendText(
//                            "/socket/message/send",
//                            "{\"chatId\": \"${chatId}\", " +
//                                    "\"message\": \"$chatMessage\"}"
//                        )
//                    }
//                    sendMsgCoroutine.launch {
//
//                    }

                }
            ) {
                Text(stringResource(Res.string.chat_send))
            }
        }


        LazyColumn(
            modifier = Modifier.height(1200.dp)
//        modifier = Modifier.verticalScroll(),
//        verticalArrangement = Arrangement.SpaceBetween
        ) {
            item {

            }
//
        }


    }

}


@Composable
fun MessageCard(item: UserChatMsgDto) {

    Row(modifier = Modifier.padding(all = 8.dp)) {
//        Image(
//            painter = rememberAsyncImagePainter(item.sendUserAvatar),
//            contentDescription = null,
//            modifier = Modifier
//                .size(40.dp)
//                .clip(CircleShape)
//                .border(1.5.dp, MaterialTheme.colorScheme.secondary, CircleShape)
//        )
        Spacer(modifier = Modifier.width(8.dp))

        // We keep track if the message is expanded or not in this
        // variable
        var isExpanded by remember { mutableStateOf(false) }
        // surfaceColor will be updated gradually from one color to the other
        val surfaceColor by animateColorAsState(
            if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        )

        // We toggle the isExpanded variable when we click on this Column
        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
            Text(
                text = item.sendUserNickname ?: "",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 1.dp,
                // surfaceColor color will be changing gradually from primary to surface
                color = surfaceColor,
                // animateContentSize will change the Surface size gradually
                modifier = Modifier.animateContentSize().padding(1.dp)
            ) {
                Text(
                    text = item.message ?: "",
                    modifier = Modifier.padding(all = 4.dp),
                    // If the message is expanded, we display all its content
                    // otherwise we only display the first line
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}


