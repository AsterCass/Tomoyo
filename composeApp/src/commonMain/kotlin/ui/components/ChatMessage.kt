package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import biz.copyToClipboard
import biz.getLastTimeInChatting
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.LocalPlatformContext
import com.github.panpf.sketch.request.ComposableImageRequest
import com.github.panpf.sketch.request.ImageRequest
import com.github.panpf.sketch.request.ImageResult
import com.github.panpf.sketch.resize.Precision
import com.github.panpf.sketch.resize.Scale
import com.github.panpf.sketch.sketch
import constant.BASE_SERVER_ADDRESS_STATIC
import constant.BaseResText
import constant.MAX_TIME_SPE_SEC
import constant.enums.NotificationType
import data.UserChatMsgDto
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toInstant
import org.jetbrains.compose.resources.stringResource
import theme.baseBackgroundBlack
import theme.pureColor
import theme.subTextColor
import tomoyo.composeapp.generated.resources.Res
import tomoyo.composeapp.generated.resources.chat_copy
import tomoyo.composeapp.generated.resources.chat_relay
import kotlin.math.absoluteValue


@OptIn(FormatStringsInDatetimeFormats::class)
fun newMessageLabel(time: String?, lastTime: String?): String {
    val ret = getLastTimeInChatting(time)
    if (null == lastTime || null == time) {
        return ret
    }
    val systemTZ = TimeZone.currentSystemDefault()

    val thisSendDateTime = LocalDateTime.Format {
        byUnicodePattern("yyyy-MM-dd HH:mm:ss")
    }.parse(time)

    val lastSendDateTime = LocalDateTime.Format {
        byUnicodePattern("yyyy-MM-dd HH:mm:ss")
    }.parse(lastTime)

    val waitLastSec = (thisSendDateTime.toInstant(systemTZ).epochSeconds -
            lastSendDateTime.toInstant(systemTZ).epochSeconds).absoluteValue
    return if (waitLastSec < MAX_TIME_SPE_SEC) "" else ret
}

@OptIn(FormatStringsInDatetimeFormats::class)
fun messageTimeLabelBuilder(
    list: List<UserChatMsgDto>,
) {
    if (list.isEmpty()) return
    val systemTZ = TimeZone.currentSystemDefault()

    val lastIndex = list.size - 1
    var lastTime: LocalDateTime? = null
    for (count in lastIndex downTo 0) {

        val thisSendDateTime = LocalDateTime.Format {
            byUnicodePattern("yyyy-MM-dd HH:mm:ss")
        }.parse(list[count].sendDate ?: continue)

        val alreadyBuildTime = null != list[count].webChatLabel
        list[count].webChatLabel = getLastTimeInChatting(list[count].sendDate)

        if (null != lastTime) {
            val waitLastSec = (thisSendDateTime.toInstant(systemTZ).epochSeconds -
                    lastTime.toInstant(systemTZ).epochSeconds).absoluteValue

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
//    val regex = Regex("\\[#b[0-9][0-9]]")
//    regex.findAll(text).forEach { result ->
//        val match = result.value
//        if (biliEmojiMap.containsKey(match)) {
//            builder.append(text.substring(currentIndex, result.range.first))
//            builder.appendInlineContent(EMOJI_REPLACE_KEY, match)
//            currentIndex = result.range.last + 1
//        }
//    }

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


@Composable
fun MessageCard(
    isLatest: Boolean,
    item: UserChatMsgDto,
    thisUserId: String,
) {


    val isSelf = thisUserId == item.sendUserId

    Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp).fillMaxWidth()) {

        if (!item.webChatLabel.isNullOrBlank()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = item.webChatLabel ?: "",
                    color = MaterialTheme.colorScheme.subTextColor,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = if (isLatest) 10.dp else 0.dp),
            verticalAlignment = Alignment.Top,
        ) {
            if (isSelf) {
                Column(
                    modifier = Modifier.weight(1f).padding(
                        start = 20.dp,
                        end = 5.dp,
                    ),
                    horizontalAlignment = Alignment.End
                ) {
                    MessageCardBody(
                        item = item,
                        bgColor = MaterialTheme.colorScheme.primary,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
                MessageCardAvtar(item.sendUserAvatar)
            } else {
                MessageCardAvtar(item.sendUserAvatar)
                Column(
                    modifier = Modifier.weight(1f).padding(
                        start = 0.dp,
                        end = 20.dp,
                    ),
                    horizontalAlignment = Alignment.Start,
                ) {
                    MessageCardBody(
                        item = item,
                        bgColor = MaterialTheme.colorScheme.pureColor,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }
        }

    }
}

@Composable
private fun MessageCardBody(
    item: UserChatMsgDto,
    bgColor: Color,
    color: Color,
) {
    val baseHeight = 35.dp
    var showPopup by remember { mutableStateOf(false) }

    Text(
        text = item.sendUserNickname ?: "",
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.titleSmall
    )

    //if(isMobile) MessageCardMobile(item, isSelf) else
    Spacer(modifier = Modifier.height(5.dp))

    val isImageMessage = item.message?.startsWith(BASE_SERVER_ADDRESS_STATIC) ?: false
    val actualBgColor = if (isImageMessage) Color.Transparent else bgColor

    Surface(
        shape = MaterialTheme.shapes.small,
        shadowElevation = 1.dp,
        color = actualBgColor,
        modifier = Modifier.padding(1.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = { _ ->
                        showPopup = true
                    },
                    onLongPress = { _ ->
                        showPopup = true
                    }
                )
            }
    ) {

        if (isImageMessage) {
            MessageCardBodyImage(item.message)
        } else {
            if (showPopup) {
                val baseHeightPx =
                    with(LocalDensity.current) { (baseHeight + 5.dp).toPx() }
                Popup(
                    alignment = Alignment.TopCenter,
                    offset = IntOffset(0, -baseHeightPx.toInt()),
                    onDismissRequest = {
                        showPopup = false
                    }
                ) {
                    MessageCardOperation(item, baseHeight) {
                        showPopup = false
                    }
                }
            }
            Text(
                text = item.message ?: "",
                modifier = Modifier.padding(
                    horizontal = 8.dp,
                    vertical = 4.dp
                ),
                style = MaterialTheme.typography.bodyLarge,
                color = color
            )
        }
    }
}

@Composable
private fun MessageCardBodyImage(url: String?) {

    val bodyImageLoadCoroutine = rememberCoroutineScope()
    var metaData by remember { mutableStateOf(Pair(0.0f, 0)) }

    val request = ComposableImageRequest(uri = url) {
        size(5000, 5000)
        precision(Precision.LESS_PIXELS)
        scale(Scale.CENTER_CROP)
    }
    if (metaData.second == 0) {
        val localPlatformContext = LocalPlatformContext.current
        localPlatformContext.sketch.enqueue(request)
        bodyImageLoadCoroutine.launch {
            val imageResult: ImageResult = localPlatformContext.sketch.execute(request)
            val width = imageResult.image?.width
            val height = imageResult.image?.height
            if (null != width && null != height) {
                val radio = width.toFloat().div(height.toFloat())
                metaData = Pair(radio, width)
            }
        }
    } else {
        AsyncImage(
            request = request,
            contentDescription = null,
            modifier = Modifier.width((metaData.second).dp).aspectRatio(metaData.first)
        )
    }

}


@Composable
private fun MessageCardAvtar(avtarUrl: String?) {
    // Context for image
    val localPlatformContext = LocalPlatformContext.current

    Row {
        AsyncImage(
            request = ImageRequest(
                context = localPlatformContext,
                uri = avtarUrl,
            ),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 10.dp, end = 5.dp)
                .size(45.dp)
                .align(Alignment.CenterVertically)
                .clip(CircleShape)
        )
    }
}

//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//private fun MessageCardMobile(
//    item: UserChatMsgDto,
//    isSelf: Boolean,
//    baseHeight: Dp,
//) {
//    val tooltipState = rememberTooltipState(isPersistent = true)
//    TooltipBox(
//        positionProvider = TooltipDefaults
//            .rememberPlainTooltipPositionProvider(),
//        tooltip = {
//            MessageCardOperation(item, baseHeight) {
//                tooltipState.dismiss()
//            }
//        },
//        state = tooltipState,
//    ) {
//        Surface(
//            shape = MaterialTheme.shapes.small,
//            shadowElevation = 1.dp,
//            color = if (isSelf) MaterialTheme.colorScheme.primary
//            else MaterialTheme.colorScheme.pureColor,
//            modifier = Modifier.animateContentSize().padding(1.dp)
//        ) {
//            val annotatedString = parseTextWithEmojis(item.message ?: "")
//            Text(
//                text = annotatedString,
//                modifier = Modifier.padding(
//                    horizontal = 8.dp,
//                    vertical = 4.dp
//                ),
//                style = MaterialTheme.typography.bodyLarge,
////                inlineContent = ANN_TEXT_MAP,
//                color = if (isSelf) MaterialTheme.colorScheme.onPrimary
//                else MaterialTheme.colorScheme.onBackground
//            )
//        }
//    }
//}


@Composable
private fun MessageCardOperation(
    item: UserChatMsgDto,
    baseHeight: Dp,
    dismiss: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(bottom = 10.dp, start = 5.dp, end = 5.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(MaterialTheme.colorScheme.baseBackgroundBlack)
            .height(baseHeight),
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
                dismiss()
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
                dismiss()
            }
        ) {
            Text(
                text = stringResource(Res.string.chat_relay),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}