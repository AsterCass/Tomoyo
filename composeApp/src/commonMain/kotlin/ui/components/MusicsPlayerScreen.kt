package ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.model.MainScreenModel
import data.model.MusicScreenModel
import org.koin.compose.koinInject

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

        //next
        if (curPosition >= totalDuration) {
            musicScreenModel.onNext()
        }


        AnimatedVisibility(
            visible = !loadingScreen,
            enter = fadeIn(animationSpec = tween(durationMillis = 1000)) + scaleIn(
                initialScale = 0.2f, animationSpec = tween(durationMillis = 1000),
            ),
        ) {
            Column(
                Modifier.fillMaxSize()
                    .padding(vertical = 4.dp, horizontal = 20.dp),
            ) {
                Box(
                    Modifier.weight(0.07f),
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

                    Button(onClick = {
                        if (isPlaying) {
                            musicScreenModel.onPause()
                        } else {
                            musicScreenModel.onPlay()
                        }
                    }, modifier = Modifier.align(Alignment.CenterHorizontally).padding(5.dp)) {
                        Text(
                            if (isPlaying) "Pause" else "Play"
                        )
                    }

                    Slider(
                        value = curPosition.toFloat(),
                        onValueChange = {
                            curPosition = it.toDouble()
                            musicScreenModel.onSeek(curPosition)
                        },
                        valueRange = 0f..totalDuration.toFloat(),
                        modifier = Modifier.align(Alignment.CenterHorizontally).padding(5.dp)
                            .width(500.dp),
                    )

                }

            }


        }


    }

}