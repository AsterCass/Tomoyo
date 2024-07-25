package ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import constant.enums.MusicPlayModel

@Composable
fun MusicsPlayerScreen(
    currentTime: Double,
    totalDuration: Double,
    isPlaying: Boolean,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onPlay: () -> Unit,
    onSeek: (Double) -> Unit,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    onChangePlayModel: (MusicPlayModel) -> Unit,
) {

    var curPosition by remember { mutableStateOf(0.0) }
    curPosition = currentTime

    Column {

        Button(onClick = {
            onStart()
        }, modifier = Modifier.align(Alignment.CenterHorizontally).padding(5.dp)) {
            Text(
                "Select one"
            )
        }

        Button(onClick = {
            if (isPlaying) {
                onPause()
            } else {
                onPlay()
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
                onSeek(curPosition)
            },
            valueRange = 0f..totalDuration.toFloat(),
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(5.dp).width(500.dp),
        )


    }

}