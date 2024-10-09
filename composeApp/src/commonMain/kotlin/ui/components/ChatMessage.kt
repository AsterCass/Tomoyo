package ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import biz.copyToClipboard
import biz.getLastTimeInChatting
import cn.hutool.core.date.DatePattern
import cn.hutool.core.date.DateTime
import constant.ANN_TEXT_MAP
import constant.BaseResText
import constant.EMOJI_REPLACE_KEY
import constant.MAX_TIME_SPE_SEC
import constant.biliEmojiMap
import constant.enums.NotificationType
import data.UserChatMsgDto
import org.jetbrains.compose.resources.stringResource
import theme.baseBackgroundBlack
import theme.pureColor
import theme.subTextColor
import theme.third
import tomoyo.composeapp.generated.resources.Res
import tomoyo.composeapp.generated.resources.chat_copy
import tomoyo.composeapp.generated.resources.chat_relay
import java.time.Duration
import java.time.LocalDateTime
import kotlin.math.absoluteValue


fun newMessageLabel(time: String?, lastTime: String?): String {
    val ret = getLastTimeInChatting(time)
    if (null == lastTime) {
        return ret
    }
    val thisSendDateTime = DateTime(
        time,
        DatePattern.NORM_DATETIME_FORMAT
    ).toLocalDateTime()
    val lastSendDateTime = DateTime(
        lastTime,
        DatePattern.NORM_DATETIME_FORMAT
    ).toLocalDateTime()
    val waitLastSec = Duration.between(thisSendDateTime, lastSendDateTime).seconds.absoluteValue
    return if (waitLastSec < MAX_TIME_SPE_SEC) "" else ret
}

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
            if (waitLastSec.absoluteValue < MAX_TIME_SPE_SEC) {
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

//    var thisCharSite = 0
//    text.codePoints().forEach { codePoint ->
//        println(codePoint)
//        if (codePoint in 57344..59647) {
//            val match = codePoint.toChar().toString()
//            if (biliEmojiMap.containsKey(match)) {
//                builder.append(text.substring(currentIndex, thisCharSite))
//                builder.appendInlineContent(EMOJI_REPLACE_KEY, match)
//                currentIndex = thisCharSite + 1
//            }
//        }
//        ++thisCharSite;
//    }

    if (currentIndex < text.length) {
        builder.append(text.substring(currentIndex))
    }
    return builder.toAnnotatedString()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageCard(item: UserChatMsgDto, thisUserId: String) {

    val isSelf = thisUserId == item.sendUserId
    val tooltipState = rememberTooltipState(isPersistent = true)

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

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = if (isSelf) Alignment.End else Alignment.Start,
        ) {


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
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = Modifier.height(5.dp))

            TooltipBox(
                positionProvider = TooltipDefaults
                    .rememberPlainTooltipPositionProvider(),
                tooltip = {
                    Row(
                        modifier = Modifier
                            .padding(bottom = 10.dp, start = 5.dp, end = 5.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(MaterialTheme.colorScheme.baseBackgroundBlack)
                            .height(35.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center

                    ) {
                        TextButton(
                            shape = RoundedCornerShape(5.dp),
                            contentPadding = PaddingValues(0.dp),
                            onClick = {
                                copyToClipboard(item.message ?: "")
                                NotificationManager.showNotification(
                                    MainNotification(
                                        BaseResText.copyTip,
                                        NotificationType.SUCCESS
                                    )
                                )
                                tooltipState.dismiss()
                            },
                        ) {
                            Text(
                                text = stringResource(Res.string.chat_copy),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimary,
                            )
                        }

                        VerticalDivider(
                            modifier = Modifier
                                .padding(0.dp).height(12.dp)
                        )

                        TextButton(
                            shape = RoundedCornerShape(5.dp),
                            contentPadding = PaddingValues(0.dp),
                            onClick = {
                                NotificationManager.createDialogAlert(
                                    MainDialogAlert(
                                        message = BaseResText.underDevelopment,
                                        cancelOperationText = BaseResText.cancelBtn
                                    )
                                )
                                tooltipState.dismiss()
                            }
                        ) {
                            Text(
                                text = stringResource(Res.string.chat_relay),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                    }

                },
                state = tooltipState,

                ) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    shadowElevation = 1.dp,
                    color = if (isSelf) MaterialTheme.colorScheme.third
                    else MaterialTheme.colorScheme.pureColor,
                    modifier = Modifier.animateContentSize().padding(1.dp)
                ) {
                    val annotatedString = parseTextWithEmojis(item.message ?: "")
                    Text(
                        text = annotatedString,
                        modifier = Modifier.padding(
                            horizontal = 8.dp,
                            vertical = 4.dp
                        ),
                        style = MaterialTheme.typography.bodyLarge,
                        inlineContent = ANN_TEXT_MAP,
                        color = if (isSelf) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onBackground
                    )
                }
            }


        }
    }
}