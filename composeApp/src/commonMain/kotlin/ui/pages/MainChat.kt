package ui.pages

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import constant.biliEmojiMap
import constant.emojiReplaceKey
import data.UserChatMsgDto
import data.model.MainScreenModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import tomoyo.composeapp.generated.resources.Res
import tomoyo.composeapp.generated.resources.bili_00
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


fun parseTextWithEmojis(text: String): AnnotatedString {
    val builder = AnnotatedString.Builder()
    var currentIndex = 0
    val regex = Regex("\\[#b[0-9][0-9]]")
    regex.findAll(text).forEach { result ->
        val match = result.value
        if (biliEmojiMap.containsKey(match)) {
            builder.append(text.substring(currentIndex, result.range.first))
            builder.appendInlineContent(emojiReplaceKey, match)
            currentIndex = result.range.last + 1
        }
    }
    if (currentIndex < text.length) {
        builder.append(text.substring(currentIndex))
    }
    return builder.toAnnotatedString()
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


        // surfaceColor will be updated gradually from one color to the other
        val surfaceColor by animateColorAsState(
            MaterialTheme.colorScheme.surface
        )

        // We toggle the isExpanded variable when we click on this Column
        Column {
            Text(
                text = item.sendUserNickname ?: "",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 1.dp,
                color = surfaceColor,
                modifier = Modifier.animateContentSize().padding(1.dp)
            ) {
                val annotatedString = parseTextWithEmojis(item.message ?: "")
                Text(
                    text = annotatedString,
                    modifier = Modifier.padding(all = 4.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    inlineContent = mapOf(
                        emojiReplaceKey to InlineTextContent(
                            Placeholder(20.sp, 20.sp, PlaceholderVerticalAlign.TextCenter)
                        ) { emoji ->
                            Image(
                                painter = painterResource(
                                    biliEmojiMap[emoji] ?: Res.drawable.bili_00
                                ),
                                modifier = Modifier.fillMaxSize(),
                                contentDescription = null,
                            )
                        }
                    )
                )
            }
        }
    }
}


