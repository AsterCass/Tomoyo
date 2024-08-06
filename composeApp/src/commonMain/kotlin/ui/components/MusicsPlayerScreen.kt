package ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import biz.formatSeconds
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.regular.ArrowAltCircleDown
import compose.icons.fontawesomeicons.regular.CommentDots
import compose.icons.fontawesomeicons.regular.PauseCircle
import compose.icons.fontawesomeicons.regular.PlayCircle
import compose.icons.fontawesomeicons.regular.ShareSquare
import compose.icons.fontawesomeicons.regular.ThumbsUp
import compose.icons.fontawesomeicons.solid.StepBackward
import compose.icons.fontawesomeicons.solid.StepForward
import constant.enums.MusicPlayModel
import data.model.MainScreenModel
import data.model.MusicScreenModel
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import theme.subTextColor
import tomoyo.composeapp.generated.resources.Res
import tomoyo.composeapp.generated.resources.nezuko

class MusicsPlayerScreen : Screen {

    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        val mainModel: MainScreenModel = koinInject()
        val musicScreenModel: MusicScreenModel = koinInject()

        //navigation
        val navigator = LocalNavigator.currentOrThrow
        val loadingScreen = mainModel.loadingScreen.collectAsState().value
        val token = mainModel.userState.value.token

        //data
        val isPlaying = musicScreenModel.playerState.collectAsState().value.isPlaying
        val totalDuration = musicScreenModel.playerState.collectAsState().value.totalDuration
        var curPosition = musicScreenModel.playerState.collectAsState().value.currentTime
        val playModel = musicScreenModel.playerState.collectAsState().value.playModel
        val currentMusicData = musicScreenModel.getCurrentMusicData()

        //animation
        var lastAngle by remember { mutableStateOf(0f) }
        val infiniteTransition = rememberInfiniteTransition()
        val rotationAngle by infiniteTransition.animateFloat(
            initialValue = lastAngle,
            targetValue = if (isPlaying) 360f else lastAngle,
            animationSpec = infiniteRepeatable(
                animation = tween(20000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )

        println("reload xxxxxxxxxx" + System.currentTimeMillis())

        AnimatedVisibility(
            visible = !loadingScreen,
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> fullHeight },
                animationSpec = tween(durationMillis = 1500),
            ),
        ) {
            Column(
                Modifier.fillMaxSize()
                    .padding(top = 4.dp),
            ) {
                Box(
                    Modifier.weight(0.07f).padding(horizontal = 20.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Button(
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                        contentPadding = PaddingValues(0.dp),
                        onClick = { navigator.pop() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null,
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(0.93f)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        painter = painterResource(Res.drawable.nezuko),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(vertical = 30.dp, horizontal = 20.dp)
                            .size(300.dp)
                            .rotate(rotationAngle)
                            .clip(CircleShape)
                            .border(
                                border = BorderStroke(2.dp, MaterialTheme.colorScheme.onBackground),
                                shape = CircleShape
                            )
                    )

                    MainBaseCardBox(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .height(50.dp)
                            .width(150.dp)

                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp)
                                .fillMaxSize(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Icon(
                                imageVector = FontAwesomeIcons.Regular.ShareSquare,
                                contentDescription = null,
                                modifier = Modifier.fillMaxHeight().clickable { },
                                tint = MaterialTheme.colorScheme.onBackground,
                            )
                            Icon(
                                imageVector = FontAwesomeIcons.Regular.ThumbsUp,
                                contentDescription = null,
                                modifier = Modifier.fillMaxHeight().clickable { },
                            )
                            Icon(
                                imageVector = FontAwesomeIcons.Regular.ArrowAltCircleDown,
                                contentDescription = null,
                                modifier = Modifier.fillMaxHeight().clickable { },
                            )
                        }

                    }

                    Column(
                        modifier = Modifier.fillMaxSize()
                            .padding(top = 30.dp)
                            .clip(
                                RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                            )
                            .border(
                                border = BorderStroke(
                                    2.dp,
                                    MaterialTheme.colorScheme.onBackground
                                ),
                                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                            )
                            .padding(15.dp)
                    ) {

                        Text(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            text = currentMusicData.audioAuthor,
                            color = MaterialTheme.colorScheme.subTextColor,
                            style = MaterialTheme.typography.bodyMedium,
                        )

                        Text(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            text = currentMusicData.audioName,
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.headlineSmall,
                        )

                        Slider(
                            value = curPosition.toFloat(),
                            onValueChange = {
                                curPosition = it.toDouble()
                                musicScreenModel.onSeek(curPosition)
                                lastAngle = rotationAngle
                            },
                            valueRange = 0f..totalDuration.toFloat(),
                            modifier = Modifier.align(Alignment.CenterHorizontally).padding(5.dp)
                                .width(500.dp),

                            )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(formatSeconds(curPosition.toInt()))
                            Text(formatSeconds(totalDuration.toInt()))
                        }

                        Row(
                            modifier = Modifier.fillMaxSize().padding(horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Icon(
                                imageVector = MusicPlayModel.entries[playModel].imageVector,
                                contentDescription = null,
                                modifier = Modifier.size(25.dp).clickable {
                                    musicScreenModel.nextPlayModel()
                                },
                                tint = MaterialTheme.colorScheme.onBackground,
                            )
                            Icon(
                                imageVector = FontAwesomeIcons.Solid.StepBackward,
                                contentDescription = null,
                                modifier = Modifier.size(25.dp).clickable {
                                    musicScreenModel.onPrev()
                                },
                            )
                            Icon(
                                imageVector = if (isPlaying) FontAwesomeIcons.Regular.PauseCircle
                                else FontAwesomeIcons.Regular.PlayCircle,
                                contentDescription = null,
                                modifier = Modifier.size(50.dp).clickable {
                                    if (isPlaying) {
                                        musicScreenModel.onPause()
                                        lastAngle = rotationAngle
                                    } else {
                                        musicScreenModel.onPlay()
                                    }
                                },
                            )
                            Icon(
                                imageVector = FontAwesomeIcons.Solid.StepForward,
                                contentDescription = null,
                                modifier = Modifier.size(25.dp).clickable {
                                    musicScreenModel.onNext()
                                },
                            )
                            Icon(
                                imageVector = FontAwesomeIcons.Regular.CommentDots,
                                contentDescription = null,
                                modifier = Modifier.size(25.dp).clickable { },
                            )


                        }


                    }


//                    Button(onClick = {
//                        if (isPlaying) {
//                            musicScreenModel.onPause()
//                        } else {
//                            musicScreenModel.onPlay()
//                        }
//                    }, modifier = Modifier.align(Alignment.CenterHorizontally).padding(5.dp)) {
//                        Text(
//                            if (isPlaying) "Pause" else "Play"
//                        )
//                    }
//

//
//                    Button(
//                        modifier = Modifier.height(50.dp).width(200.dp),
//                        onClick = {
//                            musicScreenModel.nextPlayModel()
//                        },
//                    ) {
//                        Row {
//                            Text(stringResource(MusicPlayModel.entries[playModel].desc))
//                            Icon(
//                                imageVector = MusicPlayModel.entries[playModel].imageVector,
//                                contentDescription = null,
//                                modifier = Modifier.size(50.dp)
//                            )
//                        }
//                    }
//
//                    Button(
//                        modifier = Modifier.height(80.dp).width(80.dp),
//                        onClick = {
//                            musicScreenModel.onNext()
//                        }
//                    ) {
//                        Icon(
//                            imageVector = FontAwesomeIcons.Solid.StepForward,
//                            contentDescription = null,
//                            modifier = Modifier.size(40.dp),
//                        )
//                    }
//                    Button(
//                        modifier = Modifier.height(80.dp).width(80.dp),
//                        onClick = {
//                            musicScreenModel.onPrev()
//                        }
//                    ) {
//                        Icon(
//                            imageVector = FontAwesomeIcons.Solid.StepBackward,
//                            contentDescription = null,
//                            modifier = Modifier.size(40.dp),
//                        )
//                    }

                }

            }


        }


    }

}