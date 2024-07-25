package ui.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import data.MusicSimpleModel
import org.jetbrains.compose.resources.painterResource
import theme.subTextColor
import tomoyo.composeapp.generated.resources.Res
import tomoyo.composeapp.generated.resources.nezuko
import ui.components.MainBaseCardBox


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMusicsScreen(
    constraints: Constraints,
    playList: List<MusicSimpleModel>,
    currentTime: Double,
    totalDuration: Double,
    isPlaying: Boolean,
    onStart: (String) -> Unit,
    onPause: () -> Unit,
    onPlay: () -> Unit,
    currentId: String? = null,
) {

    val density = LocalDensity.current
    val minHeightDp = with(density) { constraints.minHeight.toDp() }
    val minWidthDp = with(density) { constraints.minWidth.toDp() }

    var curPosition by remember { mutableStateOf(0.0) }
    curPosition = currentTime

    Box(
        modifier = Modifier
            .height(minHeightDp)
            .width(minWidthDp)
    ) {


        Column(
            modifier = Modifier.fillMaxSize()
                .padding(start = 10.dp, end = 10.dp)
        ) {
            //search


            LazyColumn {

                items(playList.size) { index ->
                    MusicListItem(
                        item = playList[index],
                        onStart = onStart
                    )
                }

                item {
                    Column(
                        modifier = Modifier.height(150.dp)
                    ) {
                        Text("底部背景图，没想好放什么")
                    }
                }

            }


        }




        Box(
            modifier = Modifier.align(Alignment.BottomCenter)
                .padding(15.dp)
                .height(110.dp)
                .width(minWidthDp - 50.dp)
        ) {
            MainBaseCardBox(
                modifier = Modifier.fillMaxSize()
                    .padding(5.dp)
            ) {

                Column(
                    modifier = Modifier.clickable {
                        println("进入详情页")
                    }
                ) {

                    MusicPlayItem(
                        item = playList[0],
                        isPlaying = isPlaying,
                        onPause = onPause,
                        onPlay = onPlay,
                    )

                    Slider(
                        value = curPosition.toFloat(),
                        onValueChange = { curPosition = it.toDouble() },
                        valueRange = 0f..totalDuration.toFloat(),
                        enabled = false,
                        thumb = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally).padding(5.dp)
                    )


                }

            }
        }
    }

}


@Composable
fun MusicListItem(
    item: MusicSimpleModel,
    onStart: (String) -> Unit,
) {

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp))
            .clickable {
                onStart(item.musicUrl ?: "")
            }
            .padding(10.dp)
    ) {


        Image(
            painter = painterResource(Res.drawable.nezuko),
            contentDescription = null,
            modifier = Modifier
                .weight(0.15f)
                .align(Alignment.CenterVertically)
                .clip(RoundedCornerShape(15.dp))
                .border(
                    border = BorderStroke(2.dp, Color.Black),
                    shape = RoundedCornerShape(15.dp)
                )
        )

        Column(
            modifier = Modifier.weight(0.75f)
                .align(Alignment.CenterVertically)
                .padding(start = 20.dp)
        ) {
            Text(
                modifier = Modifier.padding(start = 2.dp, bottom = 3.dp),
                text = item.musicName ?: "",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                modifier = Modifier.padding(start = 3.dp, bottom = 3.dp),
                text = item.musicAuthor ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.subTextColor
            )

        }

        Icon(
            imageVector = Icons.Outlined.FavoriteBorder,
            contentDescription = null,
            modifier = Modifier
                .weight(0.1f)
                .clip(CircleShape)
                .clickable { println("播放音乐2") }
                .align(Alignment.CenterVertically)
                .size(30.dp)
        )

    }

}

@Composable
fun MusicPlayItem(
    item: MusicSimpleModel,
    isPlaying: Boolean,
    onPause: () -> Unit,
    onPlay: () -> Unit,
) {

    Row(
        modifier = Modifier
            .padding(10.dp)
    ) {


        Image(
            painter = painterResource(Res.drawable.nezuko),
            contentDescription = null,
            modifier = Modifier
                .weight(0.15f)
                .align(Alignment.CenterVertically)
                .clip(RoundedCornerShape(15.dp))
                .border(
                    border = BorderStroke(2.dp, Color.Black),
                    shape = RoundedCornerShape(15.dp)
                )
        )

        Column(
            modifier = Modifier.weight(0.75f)
                .align(Alignment.CenterVertically)
                .padding(start = 20.dp)
        ) {
            Text(
                modifier = Modifier.padding(start = 2.dp, bottom = 3.dp),
                text = item.musicName ?: "",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                modifier = Modifier.padding(start = 3.dp, bottom = 3.dp),
                text = item.musicAuthor ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.subTextColor
            )

        }

        Icon(
            imageVector = Icons.Rounded.PlayArrow,
            contentDescription = null,
            modifier = Modifier
                .weight(0.1f)
                .clip(CircleShape)
                .clickable {
                    if (isPlaying) {
                        onPause()
                    } else {
                        onPlay()
                    }
                }
                .align(Alignment.CenterVertically)
                .size(30.dp)
        )

    }

}


