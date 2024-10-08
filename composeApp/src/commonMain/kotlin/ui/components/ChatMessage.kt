package ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import biz.getLastTimeInChatting
import cn.hutool.core.date.DatePattern
import cn.hutool.core.date.DateTime
import constant.EMOJI_REPLACE_KEY
import constant.biliEmojiMap
import data.UserChatMsgDto
import org.jetbrains.compose.resources.painterResource
import theme.subTextColor
import tomoyo.composeapp.generated.resources.Res
import tomoyo.composeapp.generated.resources.bili_00
import java.time.Duration
import java.time.LocalDateTime
import kotlin.math.absoluteValue


fun messageTimeLabelBuilder(
    list: List<UserChatMsgDto>,
) {
    if (list.isEmpty()) return

    val lastIndex = list.size - 1
    var lastTime: LocalDateTime? = null
    for (count in lastIndex downTo 0) {

        val thisSendDateTime = DateTime(
            list[count].sendDate,
            DatePattern.NORM_DATETIME_FORMAT
        ).toLocalDateTime()
        val alreadyBuildTime = null != list[count].webChatLabel
        list[count].webChatLabel = getLastTimeInChatting(list[count].sendDate)

        if (null != lastTime) {
            val waitLastSec = Duration.between(thisSendDateTime, lastTime).seconds
            if (waitLastSec.absoluteValue < 600) {
                list[count].webChatLabel = ""
            }
        }
        if (alreadyBuildTime) {
            break
        }
        lastTime = thisSendDateTime
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
            builder.appendInlineContent(EMOJI_REPLACE_KEY, match)
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

    Row(modifier = Modifier.padding(all = 8.dp).fillMaxWidth()) {
//        Image(
//            painter = rememberAsyncImagePainter(item.sendUserAvatar),
//            contentDescription = null,
//            modifier = Modifier
//                .size(40.dp)
//                .clip(CircleShape)
//                .border(1.5.dp, MaterialTheme.colorScheme.secondary, CircleShape)
//        )
        Spacer(modifier = Modifier.width(8.dp))

        val surfaceColor by animateColorAsState(
            MaterialTheme.colorScheme.surface
        )

        Column(modifier = Modifier.fillMaxWidth()) {


            if (!item.webChatLabel.isNullOrBlank()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = item.webChatLabel ?: "",
                        color = MaterialTheme.colorScheme.subTextColor,
                        style = MaterialTheme.typography.titleSmall
                    )
                }

            }


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
                        EMOJI_REPLACE_KEY to InlineTextContent(
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