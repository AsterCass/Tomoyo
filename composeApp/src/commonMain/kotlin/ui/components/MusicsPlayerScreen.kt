package ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import data.model.MainScreenModel
import org.koin.compose.koinInject

class MusicsPlayerScreen : Screen {

    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        val mainModel: MainScreenModel = koinInject()

//        currentTime: Double,
//        totalDuration: Double,
//        isPlaying: Boolean,
//        onStart: () -> Unit,
//        onPause: () -> Unit,
//        onPlay: () -> Unit,
//        onSeek: (Double) -> Unit,
//        onNext: () -> Unit,
//        onPrev: () -> Unit,
//        onChangePlayModel: (MusicPlayModel) -> Unit,

        println(mainModel.userState.value.token)

        Column {


            Text("This is Music Player")

//        Button(onClick = {
//            onStart()
//        }, modifier = Modifier.align(Alignment.CenterHorizontally).padding(5.dp)) {
//            Text(
//                "Select one"
//            )
//        }
//
//        Button(onClick = {
//            if (isPlaying) {
//                onPause()
//            } else {
//                onPlay()
//            }
//        }, modifier = Modifier.align(Alignment.CenterHorizontally).padding(5.dp)) {
//            Text(
//                if (isPlaying) "Pause" else "Play"
//            )
//        }
//
//        Slider(
//            value = curPosition.toFloat(),
//            onValueChange = {
//                curPosition = it.toDouble()
//                onSeek(curPosition)
//            },
//            valueRange = 0f..totalDuration.toFloat(),
//            modifier = Modifier.align(Alignment.CenterHorizontally).padding(5.dp).width(500.dp),
//        )


        }
    }

}