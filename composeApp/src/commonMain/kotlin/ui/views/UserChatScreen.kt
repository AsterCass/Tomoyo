package ui.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.model.ChatScreenModel
import data.model.MainScreenModel
import data.store.DataStorageManager
import org.koin.compose.koinInject
import theme.halfTransSurfaceVariant

class UserChatScreen(
    private val userId: String,
) : Screen {

    override val key: ScreenKey = uniqueScreenKey


    @Composable
    override fun Content() {
        val mainModel: MainScreenModel = koinInject()
        val chatScreenModel: ChatScreenModel = koinInject()
        val dataStorageManager: DataStorageManager = koinInject()

        //navigation
        val navigator = LocalNavigator.currentOrThrow
        val loadingScreen = mainModel.loadingScreen.collectAsState().value

        //coroutine
        val chatApiCoroutine = rememberCoroutineScope()

        //soft keyboard
        val keyboardController = LocalSoftwareKeyboardController.current


        //data
        val ime = WindowInsets.ime
        val constraints = mainModel.mainPageContainerConstraints.collectAsState().value
        val density = LocalDensity.current
        val minHeightDp = with(density) { constraints.minHeight.toDp() }
        val imeHeightDp = with(density) { ime.getBottom(density).toDp() }


        //user data
        val userState = mainModel.userState.collectAsState().value
        val token = userState.token

        //finish login
        if (token.isBlank()) {
            navigator.pop()
            return
        }

        //chat data
        val inputContent = chatScreenModel.inputContent.collectAsState().value
        val thisInputContent = inputContent[userId] ?: ""


        AnimatedVisibility(
            visible = !loadingScreen,
            enter = fadeIn(animationSpec = tween(durationMillis = 1000)),
        ) {

            Column(
                Modifier
                    .windowInsetsPadding(WindowInsets.systemBars)
                    .fillMaxHeight()
                    .padding(vertical = 4.dp, horizontal = 15.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Row(
                    Modifier.height(50.dp),
                    verticalAlignment = Alignment.CenterVertically
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



                Row {
                    TextField(
                        value = thisInputContent,
                        placeholder = { Text("信息") }, //todo
                        onValueChange = { chatScreenModel.updateInputContent(userId, it) },


                        )
                }


            }
        }
    }
}