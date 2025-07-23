package ui.pages

import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.regular.DotCircle
import compose.icons.fontawesomeicons.regular.Heart
import compose.icons.fontawesomeicons.solid.Heart
import constant.BaseResText
import constant.enums.MusicPlayScreenTabModel
import constant.enums.ViewEnum
import data.AudioSimpleModel
import data.model.MainScreenModel
import data.model.MusicScreenModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.koinInject
import theme.deepIconColor
import theme.subTextColor
import tomoyo.composeapp.generated.resources.Res
import tomoyo.composeapp.generated.resources.logo_pro
import tomoyo.composeapp.generated.resources.media_audio
import tomoyo.composeapp.generated.resources.media_pause
import tomoyo.composeapp.generated.resources.media_play
import tomoyo.composeapp.generated.resources.play_audio_play_all
import tomoyo.composeapp.generated.resources.play_audio_playing
import tomoyo.composeapp.generated.resources.search_keyword
import tomoyo.composeapp.generated.resources.under_development
import ui.components.MainBaseCardBox
import ui.components.MainDialogAlert
import ui.components.NotificationManager
import ui.views.MusicsPlayerScreen


object MainMusicsScreen : Screen {

    override val key: ScreenKey = "${ViewEnum.TAB_MAIN_MUSICS.code}$uniqueScreenKey"

    @Composable
    override fun Content() {
        MainMusicsScreen()
    }

}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainMusicsScreen(
    screenModel: MusicScreenModel = koinInject(),
    mainModel: MainScreenModel = koinInject(),
) {
    //navigation
    val navigator = LocalNavigator.currentOrThrow

    //coroutine
    val musicApiCoroutine = rememberCoroutineScope()

    //soft keyboard
    val keyboardController = LocalSoftwareKeyboardController.current

    //data
    val favList = screenModel.favList.collectAsState().value
    val musicPlayMap = screenModel.musicPlayMap.collectAsState().value
    val currentTime = screenModel.playerState.collectAsState().value.currentTime
    val totalDuration = screenModel.playerState.collectAsState().value.totalDuration
    val isPlaying = screenModel.playerState.collectAsState().value.isPlaying
    val currentPlayId = screenModel.playerState.collectAsState().value.currentPlayId
    val currentCollectionId = screenModel.playerState.value.currentCollectionId
    val constraints = mainModel.mainPageContainerConstraints.collectAsState().value

    //layout
    val density = LocalDensity.current
    val minHeightDp = with(density) { constraints.minHeight.toDp() }
    val minWidthDp = with(density) { constraints.minWidth.toDp() }

    var curPosition by remember { mutableStateOf(0.0) }
    curPosition = currentTime

    //this data
    var searchKey by remember { mutableStateOf("") }
    val tabPageState = rememberPagerState { MusicPlayScreenTabModel.entries.size }
    val tabOrdinal = remember { derivedStateOf { tabPageState.currentPage } }
    val listStateCommon = rememberLazyListState()
    val listStateFav = rememberLazyListState()


    Box(
        modifier = Modifier
            .height(minHeightDp)
            .width(minWidthDp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(start = 10.dp, end = 10.dp)
        ) {

            TabRow(
                modifier = Modifier.fillMaxWidth(),
                selectedTabIndex = tabOrdinal.value,
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground,
                divider = {},
            ) {
                for (tabEnum in MusicPlayScreenTabModel.entries) {
                    Tab(
                        selected = tabOrdinal.value == tabEnum.ordinal,
                        onClick = {
                            screenModel.updateMusicTab(tabEnum)
                            musicApiCoroutine.launch {
                                tabPageState.animateScrollToPage(
                                    page = tabEnum.ordinal,
                                    animationSpec = tween(durationMillis = 500)
                                )
                            }
                        },
                        text = {
                            Text(
                                text = stringResource(tabEnum.text),
                                color = if (tabOrdinal.value == tabEnum.ordinal)
                                    MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        },
                        modifier = Modifier.clip(
                            RoundedCornerShape(10.dp)
                        ),
                    )
                }
            }


            HorizontalPager(
                state = tabPageState,
                verticalAlignment = Alignment.Top
            ) { page ->
                Column {
                    when (page) {
                        MusicPlayScreenTabModel.COMMON.ordinal -> {

                            MainBaseCardBox(
                                modifier = Modifier.padding(
                                    start = 10.dp,
                                    end = 10.dp,
                                    top = 12.dp,
                                    bottom = 5.dp
                                )
                                    .height(62.dp),
                                alignment = Alignment.CenterStart,
                            ) {
                                OutlinedTextField(
                                    modifier = Modifier.height(52.dp),
                                    value = searchKey,
                                    onValueChange = { searchKey = it },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        unfocusedBorderColor = Color.Transparent,
                                        focusedBorderColor = Color.Transparent,
                                    ),
                                    textStyle = MaterialTheme.typography.bodyMedium,
                                    placeholder = {
                                        Text(
                                            text = stringResource(Res.string.search_keyword),
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.subTextColor
                                        )
                                    },
                                    maxLines = 1,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text,
                                        imeAction = ImeAction.Search
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onSearch = {
                                            searchKey = ""
                                            keyboardController?.hide()
                                            NotificationManager.createDialogAlert(
                                                MainDialogAlert(
                                                    message = BaseResText.underDevelopment,
                                                    cancelOperationText = BaseResText.cancelBtn
                                                )
                                            )
                                        },
                                    ),
                                )
                            }

                            MusicGlobalPlayRow(
                                isPlaying = isPlaying,
                                musicPlayMap = musicPlayMap,
                                currentPlayId = currentPlayId,
                                screenModel = screenModel,
                                onStart = {
                                    screenModel.onStart(
                                        playListId = musicPlayMap.keys.first(),
                                        playCollectionId = MusicPlayScreenTabModel.COMMON.collectionId,
                                    )
                                },
                                isMatchCollection = currentCollectionId ==
                                        MusicPlayScreenTabModel.entries[tabOrdinal.value].collectionId
                            )


                            LazyColumn(state = listStateCommon) {

                                items(musicPlayMap.size) { index ->
                                    val playingItem = musicPlayMap.values.toList()[index]
                                    MusicListItem(
                                        favList = favList,
                                        isOnThisItem = currentPlayId == playingItem.id,
                                        isPlaying = isPlaying,
                                        item = playingItem,
                                        onStart = {
                                            screenModel.onStart(
                                                playListId = playingItem.id,
                                                playCollectionId = MusicPlayScreenTabModel.COMMON.collectionId,
                                            )
                                            if (it) {
                                                navigator.parent?.push(MusicsPlayerScreen())
                                            }
                                        },
                                        onFav = { isFav, id ->
                                            if (isFav) screenModel.addFav(id) else screenModel.delFav(
                                                id
                                            )
                                        },
                                        onPause = { screenModel.onPause() },
                                        onPlay = { screenModel.onPlay() },
                                    )
                                }

                                item {
                                    if (musicPlayMap.isEmpty()) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.padding(16.dp)
                                            )
                                        }
                                    }
                                    musicApiCoroutine.launch {
                                        musicApiCoroutine.launch { screenModel.updateAllAudioList() }
                                    }
                                }

                                item {
                                    Row(
                                        modifier = Modifier.height(150.dp).fillMaxSize()
                                            .padding(10.dp),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text("底部背景图，没想好放什么") //todo
                                    }
                                }

                            }

                        }

                        MusicPlayScreenTabModel.FAV.ordinal -> {

                            val playFavMap = musicPlayMap.filter { favList.contains(it.key) }

                            MusicGlobalPlayRow(
                                isPlaying = isPlaying,
                                musicPlayMap = musicPlayMap,
                                currentPlayId = currentPlayId,
                                screenModel = screenModel,
                                onStart = {
                                    if (playFavMap.isNotEmpty()) {
                                        screenModel.onStart(
                                            playListId = playFavMap.keys.first(),
                                            playCollectionId = MusicPlayScreenTabModel.FAV.collectionId,
                                            musicPlayMap = playFavMap,
                                        )
                                    }
                                },
                                isMatchCollection = currentCollectionId ==
                                        MusicPlayScreenTabModel.entries[tabOrdinal.value].collectionId
                            )

                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                state = listStateFav
                            ) {

                                items(playFavMap.size) { index ->
                                    val playingItem = playFavMap.values.toList()[index]
                                    MusicListItem(
                                        favList = favList,
                                        isOnThisItem = currentPlayId == playingItem.id,
                                        isPlaying = isPlaying,
                                        item = playingItem,
                                        onStart = {
                                            screenModel.onStart(
                                                playListId = playingItem.id,
                                                playCollectionId = MusicPlayScreenTabModel.FAV.collectionId,
                                                musicPlayMap = playFavMap,
                                            )
                                            if (it) {
                                                navigator.parent?.push(MusicsPlayerScreen())
                                            }
                                        },
                                        onFav = { isFav, id ->
                                            if (isFav) screenModel.addFav(id) else screenModel.delFav(
                                                id
                                            )
                                        },
                                        onPause = { screenModel.onPause() },
                                        onPlay = { screenModel.onPlay() },
                                    )
                                }


                                item {
                                    Row(
                                        modifier = Modifier.height(150.dp).fillMaxSize()
                                            .padding(10.dp),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text("底部背景图，没想好放什么") //todo
                                    }
                                }

                            }

                        }


                        MusicPlayScreenTabModel.COLLECTIONS.ordinal -> {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(stringResource(Res.string.under_development))
                            }
                        }

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
                            navigator.parent?.push(MusicsPlayerScreen())
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

                                        when (tabOrdinal.value) {
                                            MusicPlayScreenTabModel.COMMON.ordinal -> {
                                                listStateCommon.scrollToItem(
                                                    musicPlayMap.keys.indexOf(
                                                        currentPlayId
                                                    )
                                                )
                                            }

                                            MusicPlayScreenTabModel.FAV.ordinal -> {
                                                listStateFav.scrollToItem(
                                                    favList.indexOf(
                                                        currentPlayId
                                                    )
                                                )
                                            }

                                            else -> {}
                                        }

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
fun MusicGlobalPlayRow(
    isPlaying: Boolean,
    musicPlayMap: Map<String, AudioSimpleModel>,
    currentPlayId: String,
    isMatchCollection: Boolean,
    screenModel: MusicScreenModel,
    onStart: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedButton(
            modifier = Modifier.padding(start = 10.dp),
            onClick = {
                if (musicPlayMap.isNotEmpty()) {
                    if (currentPlayId.isNotBlank()) {
                        if (isPlaying) {
                            screenModel.onPause()
                        } else {
                            if (isMatchCollection) {
                                screenModel.onPlay()
                            } else {
                                onStart()
                            }
                        }
                    } else {
                        onStart()
                    }
                }
            },
            contentPadding = PaddingValues(0.dp),
            border = BorderStroke(0.dp, Color.Transparent),
            shape = RoundedCornerShape(10.dp),
        ) {
            Icon(
                imageVector = if (isPlaying) vectorResource(Res.drawable.media_pause)
                else vectorResource(Res.drawable.media_play),
                contentDescription = null,
                modifier = Modifier
                    .padding(2.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .size(28.dp),
//                tint = if (isPlaying) MaterialTheme.colorScheme.primary
//                else MaterialTheme.colorScheme.deepIconColor
            )
            Text(
                modifier = Modifier.padding(end = 10.dp),
                text = if (isPlaying) stringResource(Res.string.play_audio_playing)
                else stringResource(Res.string.play_audio_play_all),
                style = MaterialTheme.typography.bodyMedium,
                color = if (isPlaying) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onBackground,
            )

        }

        Icon(
            imageVector = vectorResource(Res.drawable.media_audio),
            contentDescription = null,
            modifier = Modifier
                .padding(end = 15.dp)
                .clip(RoundedCornerShape(10.dp))
                .clickable {
                    NotificationManager.createDialogAlert(
                        MainDialogAlert(
                            message = BaseResText.underDevelopment,
                            cancelOperationText = BaseResText.cancelBtn
                        )
                    )
                }
                .size(28.dp),
            tint = MaterialTheme.colorScheme.onBackground
        )

    }
}


@Composable
fun MusicListItem(
    favList: Set<String>,
    isOnThisItem: Boolean,
    isPlaying: Boolean,
    item: AudioSimpleModel,
    onStart: (Boolean) -> Unit,
    onFav: (Boolean, String) -> Unit,
    onPause: () -> Unit,
    onPlay: () -> Unit,
) {

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp))
            .clickable {
                onStart(true)
            }
            .padding(vertical = 10.dp)
            .fillMaxHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier.weight(0.55f)
                .align(Alignment.CenterVertically)
                .padding(start = 10.dp)
        ) {
            Text(
                modifier = Modifier.padding(start = 2.dp, bottom = 3.dp),
                text = item.audioName,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isOnThisItem) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onBackground,
            )
            Text(
                modifier = Modifier.padding(start = 3.dp, bottom = 3.dp),
                text = item.audioAuthor,
                style = MaterialTheme.typography.bodySmall,
                color = if (isOnThisItem) MaterialTheme.colorScheme.inversePrimary
                else MaterialTheme.colorScheme.subTextColor,
            )

        }

        Row(
            modifier = Modifier.weight(0.3f),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = if (favList.contains(item.id))
                    FontAwesomeIcons.Solid.Heart
                else FontAwesomeIcons.Regular.Heart,
                contentDescription = null,
                modifier = Modifier
                    .padding(2.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable {
                        onFav(!favList.contains(item.id), item.id)
                    }
                    .size(22.dp),
//                tint = if (isOnThisItem) MaterialTheme.colorScheme.primary
//                else MaterialTheme.colorScheme.deepIconColor
            )
            Icon(
                imageVector = if (isPlaying && isOnThisItem) vectorResource(Res.drawable.media_pause)
                else vectorResource(Res.drawable.media_play),
                contentDescription = null,
                modifier = Modifier
                    .padding(2.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable {
                        if (isOnThisItem) {
                            if (isPlaying) {
                                onPause()
                            } else {
                                onPlay()
                            }
                        } else {
                            onStart(false)
                        }
                    }
                    .size(28.dp),
//                tint = if (isOnThisItem) MaterialTheme.colorScheme.primary
//                else MaterialTheme.colorScheme.deepIconColor
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
            painter = painterResource(Res.drawable.logo_pro),
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
                style = MaterialTheme.typography.bodyMedium,
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
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
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


