package ui.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.regular.DotCircle
import constant.enums.NotificationType
import data.AudioSimpleModel
import data.model.MainScreenModel
import data.model.MusicScreenModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.koinInject
import theme.deepIconColor
import theme.subTextColor
import tomoyo.composeapp.generated.resources.Res
import tomoyo.composeapp.generated.resources.media_audio
import tomoyo.composeapp.generated.resources.media_pause
import tomoyo.composeapp.generated.resources.media_play
import tomoyo.composeapp.generated.resources.nezuko
import ui.components.MainBaseCardBox
import ui.components.MainNotification
import ui.components.MusicsPlayerScreen
import ui.components.NotificationManager


object MainMusicsScreen : Screen {

    private fun readResolve(): Any = MainMusicsScreen

    @Composable
    override fun Content() {
        MainMusicsScreen()
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMusicsScreen(
    screenModel: MusicScreenModel = koinInject(),
    mainModel: MainScreenModel = koinInject(),
) {
    //navigation
    mainModel.updateShowNavBar(true)
    val navigator = LocalNavigator.currentOrThrow

    //coroutine
    val musicApiCoroutine = rememberCoroutineScope()

    //data
    val musicPlayMap = screenModel.musicPlayMap.collectAsState().value
    val currentTime = screenModel.playerState.collectAsState().value.currentTime
    val totalDuration = screenModel.playerState.collectAsState().value.totalDuration
    val isPlaying = screenModel.playerState.collectAsState().value.isPlaying
    val currentPlayId = screenModel.playerState.collectAsState().value.currentPlayId
    val constraints = mainModel.mainPageContainerConstraints.collectAsState().value

    //update data
    musicApiCoroutine.launch { screenModel.updateAllAudioList() }

    //jump
    val listState = rememberLazyListState()

    //layout
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
            LazyColumn(state = listState) {

                items(musicPlayMap.size) { index ->
                    val playingItem = musicPlayMap.values.toList()[index]
                    MusicListItem(
                        isPlaying = currentPlayId == playingItem.id,
                        item = playingItem,
                        onStart = {
                            screenModel.onStart(playingItem.id)
                            if (it) {
                                navigator.push(MusicsPlayerScreen())
                                mainModel.updateShowNavBar(false)
                            }
                        }
                    )
                }

                item {
                    Row(
                        modifier = Modifier.height(150.dp).fillMaxSize().padding(10.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("底部背景图，没想好放什么")
                    }
                }
            }
        }

        if (musicPlayMap.isNotEmpty() && currentPlayId.isNotBlank()) {
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
                            navigator.push(MusicsPlayerScreen())
                            mainModel.updateShowNavBar(false)
                        }
                    ) {

                        Box(modifier = Modifier.weight(0.8f).fillMaxSize()) {
                            MusicPlayItem(
                                item = musicPlayMap[currentPlayId],
                                isPlaying = isPlaying,
                                onPause = { screenModel.onPause() },
                                onPlay = { screenModel.onPlay() },
                                toPlaying = {
                                    musicApiCoroutine.launch {
                                        listState.scrollToItem(
                                            musicPlayMap.keys.indexOf(
                                                currentPlayId
                                            )
                                        )
                                    }
                                }
                            )
                        }


                        Slider(
                            value = curPosition.toFloat(),
                            onValueChange = { curPosition = it.toDouble() },
                            valueRange = 0f..totalDuration.toFloat(),
                            enabled = false,
                            thumb = {},
                            modifier = Modifier
                                .weight(0.2f)
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally).padding(
                                    top = 2.dp, start = 5.dp, end = 5.dp, bottom = 10.dp
                                ),
                        )


                    }

                }
            }
        }


    }

}


@Composable
fun MusicListItem(
    isPlaying: Boolean,
    item: AudioSimpleModel,
    onStart: (Boolean) -> Unit,
) {

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp))
            .clickable {
                onStart(true)
            }
            .padding(10.dp)
            .fillMaxHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = painterResource(Res.drawable.nezuko),
            contentDescription = null,
            modifier = Modifier
                .weight(0.15f)
                .align(Alignment.CenterVertically)
                .clip(RoundedCornerShape(15.dp))
                .border(
                    border = BorderStroke(
                        2.dp,
                        if (isPlaying) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onBackground
                    ),
                    shape = RoundedCornerShape(15.dp),
                )
        )

        Column(
            modifier = Modifier.weight(0.55f)
                .align(Alignment.CenterVertically)
                .padding(start = 20.dp)
        ) {
            Text(
                modifier = Modifier.padding(start = 2.dp, bottom = 3.dp),
                text = item.audioName,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isPlaying) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onBackground,
            )
            Text(
                modifier = Modifier.padding(start = 3.dp, bottom = 3.dp),
                text = item.audioAuthor,
                style = MaterialTheme.typography.bodySmall,
                color = if (isPlaying) MaterialTheme.colorScheme.inversePrimary
                else MaterialTheme.colorScheme.subTextColor,
            )

        }

        Row(
            modifier = Modifier.weight(0.3f),
            horizontalArrangement = Arrangement.End
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.media_audio),
                contentDescription = null,
                modifier = Modifier
                    .padding(2.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable {
                        NotificationManager.showNotification(
                            MainNotification(
                                "开发中",
                                NotificationType.SUCCESS
                            )
                        )
                    }
                    .size(28.dp),
                tint = if (isPlaying) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.deepIconColor
            )
            Icon(
                imageVector = if (isPlaying) vectorResource(Res.drawable.media_pause)
                else vectorResource(Res.drawable.media_play),
                contentDescription = null,
                modifier = Modifier
                    .padding(2.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onStart(false) }
                    .size(28.dp),
                tint = if (isPlaying) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.deepIconColor
            )
        }



    }

}

@Composable
fun MusicPlayItem(
    item: AudioSimpleModel?,
    isPlaying: Boolean,
    onPause: () -> Unit,
    onPlay: () -> Unit,
    toPlaying: () -> Unit,
) {

    if (null == item) return

    Row(
        modifier = Modifier
            .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 0.dp)
    ) {

        Image(
            painter = painterResource(Res.drawable.nezuko),
            contentDescription = null,
            modifier = Modifier
                .weight(0.15f)
                .align(Alignment.CenterVertically)
                .clip(RoundedCornerShape(15.dp))
                .border(
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.onBackground),
                    shape = RoundedCornerShape(15.dp)
                )
        )

        Column(
            modifier = Modifier.weight(0.65f)
                .align(Alignment.CenterVertically)
                .padding(start = 20.dp)
        ) {
            Text(
                modifier = Modifier.padding(start = 2.dp, bottom = 3.dp),
                text = item.audioName,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                modifier = Modifier.padding(start = 3.dp, bottom = 3.dp),
                text = item.audioAuthor,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.subTextColor,
            )

        }

        Row(
            modifier = Modifier.weight(0.2f)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = FontAwesomeIcons.Regular.DotCircle,
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable {
                        toPlaying()
                    }
                    .padding(horizontal = 2.dp)
                    .size(25.dp)
                    .padding(2.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
            Icon(
                imageVector = if (isPlaying) vectorResource(Res.drawable.media_pause)
                else vectorResource(Res.drawable.media_play),
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable {
                        if (isPlaying) {
                            onPause()
                        } else {
                            onPlay()
                        }
                    }
                    .size(28.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }


    }

}


