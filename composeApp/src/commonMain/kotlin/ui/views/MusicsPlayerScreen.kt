package ui.views

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import biz.formatSeconds
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.regular.ArrowAltCircleDown
import compose.icons.fontawesomeicons.regular.ShareSquare
import compose.icons.fontawesomeicons.regular.ThumbsUp
import constant.BaseResText
import constant.enums.MusicPlayModel
import constant.enums.ViewEnum
import data.model.MainScreenModel
import data.model.MusicScreenModel
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.koinInject
import tomoyo.composeapp.generated.resources.Res
import tomoyo.composeapp.generated.resources.logo
import tomoyo.composeapp.generated.resources.media_audio
import tomoyo.composeapp.generated.resources.media_circle_pause
import tomoyo.composeapp.generated.resources.media_circle_play
import tomoyo.composeapp.generated.resources.media_next
import tomoyo.composeapp.generated.resources.media_previous
import ui.components.MainBaseCardBox
import ui.components.MainDialogAlert
import ui.components.NotificationManager

class MusicsPlayerScreen : Screen {

    override val key: ScreenKey = "${ViewEnum.MUSIC_PLAYER.code}$uniqueScreenKey"

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val mainModel: MainScreenModel = koinInject()
        val musicScreenModel: MusicScreenModel = koinInject()

        //navigation
        val navigator = LocalNavigator.currentOrThrow
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

        Surface {
            Column(
                Modifier.windowInsetsPadding(WindowInsets.systemBars).fillMaxSize()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 3.dp, vertical = 6.dp),
                ) {

                    IconButton(
                        modifier = Modifier.align(Alignment.CenterStart),
                        onClick = { navigator.pop() }
                    ) {
                        Icon(
                            modifier = Modifier.size(25.dp),
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        imageVector = vectorResource(Res.drawable.logo),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(vertical = 30.dp, horizontal = 20.dp)
                            .size(300.dp)
                            .rotate(rotationAngle)
                            .shadow(
                                elevation = 4.dp,
                                shape = CircleShape,
                                clip = false,
                                spotColor = MaterialTheme.colorScheme.primary
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
                                        NotificationManager.createDialogAlert(
                                            MainDialogAlert(
                                                message = BaseResText.underDevelopment,
                                                cancelOperationText = BaseResText.cancelBtn
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
                                        NotificationManager.createDialogAlert(
                                            MainDialogAlert(
                                                message = BaseResText.underDevelopment,
                                                cancelOperationText = BaseResText.cancelBtn
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
                                        NotificationManager.createDialogAlert(
                                            MainDialogAlert(
                                                message = BaseResText.underDevelopment,
                                                cancelOperationText = BaseResText.cancelBtn
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
                            color = MaterialTheme.colorScheme.outline,
                            style = MaterialTheme.typography.bodyMedium,
                        )

                        Text(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            text = currentMusicData.audioName,
                            color = MaterialTheme.colorScheme.primary,
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
                            track = { sliderPositions ->
                                SliderDefaults.Track(
                                    sliderState = sliderPositions,
                                    modifier = Modifier.height(10.dp)
                                )
                            },
                            thumb =
                            {
                                Icon(
                                    imageVector = vectorResource(Res.drawable.logo),
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            },
                            colors = SliderColors(
                                thumbColor = MaterialTheme.colorScheme.outline,
                                activeTrackColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                activeTickColor = MaterialTheme.colorScheme.outline,
                                inactiveTrackColor = MaterialTheme.colorScheme.primaryContainer,
                                inactiveTickColor = MaterialTheme.colorScheme.outline,
                                disabledThumbColor = MaterialTheme.colorScheme.outline,
                                disabledActiveTrackColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                disabledActiveTickColor = MaterialTheme.colorScheme.outline,
                                disabledInactiveTrackColor = MaterialTheme.colorScheme.primaryContainer,
                                disabledInactiveTickColor = MaterialTheme.colorScheme.outline,
                            ),

                            )

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                formatSeconds(curPosition.toInt()),
                                color = MaterialTheme.colorScheme.outline
                            )
                            Text(
                                formatSeconds(totalDuration.toInt()),
                                color = MaterialTheme.colorScheme.outline
                            )
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
                                        NotificationManager.createDialogAlert(
                                            MainDialogAlert(
                                                message = BaseResText.underDevelopment,
                                                cancelOperationText = BaseResText.cancelBtn
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