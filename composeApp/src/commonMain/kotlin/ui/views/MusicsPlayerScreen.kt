package ui.views

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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
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
import androidx.compose.ui.graphics.Color
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
import compose.icons.fontawesomeicons.regular.ShareSquare
import compose.icons.fontawesomeicons.regular.ThumbsUp
import compose.icons.fontawesomeicons.solid.CandyCane
import constant.BaseResText
import constant.enums.MusicPlayModel
import constant.enums.NotificationType
import data.model.MainScreenModel
import data.model.MusicScreenModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.koinInject
import theme.halfTransSurfaceVariant
import theme.subTextColor
import tomoyo.composeapp.generated.resources.Res
import tomoyo.composeapp.generated.resources.media_audio
import tomoyo.composeapp.generated.resources.media_circle_pause
import tomoyo.composeapp.generated.resources.media_circle_play
import tomoyo.composeapp.generated.resources.media_next
import tomoyo.composeapp.generated.resources.media_previous
import tomoyo.composeapp.generated.resources.nezuko
import ui.components.MainBaseCardBox
import ui.components.MainNotification
import ui.components.NotificationManager

class MusicsPlayerScreen : Screen {

    override val key: ScreenKey = uniqueScreenKey

    @OptIn(ExperimentalMaterial3Api::class)
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
            targetValue = if (isPlaying) (360f + lastAngle) else lastAngle,
            animationSpec = infiniteRepeatable(
                animation = tween(20000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )

        AnimatedVisibility(
            visible = !loadingScreen,
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> fullHeight },
                animationSpec = tween(durationMillis = 1500),
            ),
        ) {
            Column(
                Modifier.fillMaxSize()
                    .windowInsetsPadding(WindowInsets.systemBars)
                    .padding(top = 4.dp),
            ) {
                Box(
                    Modifier.weight(0.07f).padding(horizontal = 20.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Button(
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.halfTransSurfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
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
                            modifier = Modifier.padding(vertical = 6.dp, horizontal = 10.dp)
                                .fillMaxSize(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Icon(
                                imageVector = FontAwesomeIcons.Regular.ShareSquare,
                                contentDescription = null,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(5.dp))
                                    .clickable {
                                        NotificationManager.showNotification(
                                            MainNotification(
                                                BaseResText.underDevelopment,
                                                NotificationType.SUCCESS
                                            )
                                        )
                                    }
                                    .padding(5.dp)
                                    .fillMaxHeight(),
                            )
                            Icon(
                                imageVector = FontAwesomeIcons.Regular.ThumbsUp,
                                contentDescription = null,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(5.dp))
                                    .clickable {
                                        NotificationManager.showNotification(
                                            MainNotification(
                                                BaseResText.underDevelopment,
                                                NotificationType.SUCCESS
                                            )
                                        )
                                    }
                                    .padding(5.dp)
                                    .fillMaxHeight(),
                            )
                            Icon(
                                imageVector = FontAwesomeIcons.Regular.ArrowAltCircleDown,
                                contentDescription = null,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(5.dp))
                                    .clickable {
                                        NotificationManager.showNotification(
                                            MainNotification(
                                                BaseResText.underDevelopment,
                                                NotificationType.SUCCESS
                                            )
                                        )
                                    }
                                    .padding(5.dp)
                                    .fillMaxHeight(),
                            )
                        }

                    }

                    Column(
                        modifier = Modifier.fillMaxSize()
                            .padding(top = 30.dp)
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
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            thumb =
                            {
                                Icon(
                                    imageVector = FontAwesomeIcons.Solid.CandyCane,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            colors = SliderColors(
                                thumbColor = Color.Transparent,
                                activeTrackColor = MaterialTheme.colorScheme.primary,
                                activeTickColor = Color.Transparent,
                                inactiveTrackColor = MaterialTheme.colorScheme.inversePrimary,
                                inactiveTickColor = Color.Transparent,
                                disabledThumbColor = Color.Transparent,
                                disabledActiveTrackColor = MaterialTheme.colorScheme.primary,
                                disabledActiveTickColor = Color.Transparent,
                                disabledInactiveTrackColor = MaterialTheme.colorScheme.inversePrimary,
                                disabledInactiveTickColor = Color.Transparent,
                            ),

                            )

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
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
                                imageVector = vectorResource(MusicPlayModel.entries[playModel].imageVector),
                                contentDescription = null,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        musicScreenModel.nextPlayModel()
                                    }
                                    .size(30.dp),
                            )
                            Icon(
                                imageVector = vectorResource(Res.drawable.media_previous),
                                contentDescription = null,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        musicScreenModel.onPrev()
                                    }
                                    .size(30.dp),
                            )
                            Icon(
                                imageVector = if (isPlaying) vectorResource(Res.drawable.media_circle_pause)
                                else vectorResource(Res.drawable.media_circle_play),
                                contentDescription = null,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        if (isPlaying) {
                                            musicScreenModel.onPause()
                                            lastAngle = rotationAngle
                                        } else {
                                            musicScreenModel.onPlay()
                                        }
                                    }
                                    .size(60.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Icon(
                                imageVector = vectorResource(Res.drawable.media_next),
                                contentDescription = null,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        musicScreenModel.onNext()
                                    }
                                    .size(30.dp),
                            )
                            Icon(
                                imageVector = vectorResource(Res.drawable.media_audio),
                                contentDescription = null,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        NotificationManager.showNotification(
                                            MainNotification(
                                                BaseResText.underDevelopment,
                                                NotificationType.SUCCESS
                                            )
                                        )
                                    }
                                    .size(30.dp),
                            )

                        }


                    }

                }

            }


        }


    }

}