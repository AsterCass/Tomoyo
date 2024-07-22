package ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainMusicsScreen(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onPlay: () -> Unit,
) {



    Column(
        modifier = modifier,
    ) {

        Button(onClick = {
            onStart()
        }, modifier = modifier.align(Alignment.CenterHorizontally).padding(5.dp)) {
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
        }, modifier = modifier.align(Alignment.CenterHorizontally).padding(5.dp)) {
            Text(
                if (isPlaying) "Pause" else "Play"
            )
        }


    }


}