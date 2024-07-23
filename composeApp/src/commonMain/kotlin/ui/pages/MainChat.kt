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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.ChatRowModel
import data.UserDataModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.sendText

@Composable
fun MainChatScreen(
    userData: UserDataModel = UserDataModel(),
    userDataVersion: Int = 0,
    socketSession: StompSession?,
) {

    if (userData.token.isNullOrBlank()) {
        return
    }

    //coroutine
    val sendMsgCoroutine = rememberCoroutineScope()

    var chatMessage by rememberSaveable { mutableStateOf(userDataVersion.toString()) }

    Column(
    ) {
        if (!userData.token.isNullOrBlank()) {
            TextField(
                value = chatMessage,
                placeholder = { Text("信息") },
                onValueChange = { chatMessage = it }
            )
            Button(
                onClick = {
//                    sendMsgCoroutine.launch {
                    CoroutineScope(Dispatchers.IO).launch {
                        socketSession?.sendText(
                            "/socket/message/send",
                            "{\"chatId\": \"${userData.chatId}\", " +
                                    "\"message\": \"$chatMessage\"}"
                        )
                    }
//                    sendMsgCoroutine.launch {
//
//                    }

                }
            ) {
                Text("发送")
            }
        }


        LazyColumn(
            modifier = Modifier.height(1200.dp)
//        modifier = Modifier.verticalScroll(),
//        verticalArrangement = Arrangement.SpaceBetween
        ) {
            item {

            }
            items(userData.chatRowList.size) { index ->
                MessageCard(item = userData.chatRowList[index])
            }
        }


    }

}


@Composable
fun MessageCard(item: ChatRowModel) {

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
                text = item.sendUserNickname,
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
                    text = item.sendMessage,
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