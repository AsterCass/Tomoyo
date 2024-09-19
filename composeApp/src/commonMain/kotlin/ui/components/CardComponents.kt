package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import kotlin.math.max
import kotlin.math.min

data class SwipeToRevealCardOption(
    val optionText: String,
    val optionOperation: () -> Unit,
    val width: Dp,
    val optColor: Color,
    val optBgColor: Color,
)


@Composable
fun SwipeToRevealCard(
    modifier: Modifier = Modifier,
    optionList: List<SwipeToRevealCardOption> = emptyList(),
    content: @Composable () -> Unit
) {

    var offsetX by remember { mutableStateOf(0f) }
    val maxOffsetDp = optionList.map { it.width }.reduce { acc, dp -> acc + dp }
    val maxOffset = with(LocalDensity.current) { maxOffsetDp.toPx() }

    Box(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .offset { IntOffset(offsetX.toInt() + maxOffset.toInt(), 0) },
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {

            for (opt in optionList) {
                Box(
                    modifier = Modifier.fillMaxHeight().width(opt.width).background(opt.optBgColor)
                        .clickable { opt.optionOperation }, contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier, text = opt.optionText, color = opt.optColor
                    )
                }
            }

        }

        Box(modifier = Modifier.offset { IntOffset(offsetX.toInt(), 0) }.fillMaxSize()
            .background(MaterialTheme.colorScheme.background).pointerInput(Unit) {
                detectHorizontalDragGestures(onHorizontalDrag = { _, dragAmount ->
                    val newOffset = offsetX + dragAmount
                    offsetX = max(-maxOffset, min(0f, newOffset))
                }, onDragEnd = {
                    if (offsetX < -maxOffset / 2) {
                        offsetX = (-maxOffset)
                    } else {
                        offsetX = 0f
                    }
                })
            }) {
            content()
        }
    }
}