package ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.LocalPlatformContext
import com.github.panpf.sketch.request.ImageRequest
import data.model.ContactScreenModel
import data.model.MainScreenModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import org.koin.core.qualifier.named
import theme.baseBackground
import tomoyo.composeapp.generated.resources.Res
import tomoyo.composeapp.generated.resources.bg3


class UserDetailScreen(
    private val userId: String,
) : Screen {

    override val key: ScreenKey = uniqueScreenKey


    @Composable
    override fun Content() {
        //inject
        val mainModel: MainScreenModel = koinInject()
        val contactScreenModel: ContactScreenModel = koinInject()
        val configBlock: (ImageRequest.Builder.() -> Unit) = koinInject()
        val context = LocalPlatformContext.current
        val isMobile: Boolean = koinInject(qualifier = named("isMobile"))

        //coroutine
        val userDetailApiCoroutine = rememberCoroutineScope()

        //navigation
        val navigator = LocalNavigator.currentOrThrow
        val loadingScreen = mainModel.loadingScreen.collectAsState().value

        //data
        val userDetailData = contactScreenModel.userDetail.collectAsState().value

        //this data
        val statusBarHigh = if (isMobile) 30.dp else 0.dp


        userDetailApiCoroutine.launch {
            contactScreenModel.updateUserDetail(userId)
        }

        if (userDetailData.id != userId) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(16.dp)
                )
            }
            return
        }


        AnimatedVisibility(
            visible = !loadingScreen,
            enter = fadeIn(animationSpec = tween(durationMillis = 1000)),
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(Res.drawable.bg3),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentScale = ContentScale.FillWidth,
                )



                Column(
                    Modifier.padding(
                        start = 15.dp,
                        end = 15.dp,
                        top = statusBarHigh + 4.dp,
                        bottom = 4.dp
                    ).fillMaxSize()

                ) {

                    Row(
                        Modifier.height(50.dp),
                        verticalAlignment = Alignment.CenterVertically
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
                            onClick = { navigator.popUntilRoot() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = null,
                            )
                        }
                    }

                    Box(
                        modifier = Modifier.fillMaxSize().padding(top = 20.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 50.dp, bottom = 20.dp)
                                .graphicsLayer {
                                    alpha = 0.9f
                                }
                                .clip(
                                    RoundedCornerShape(16.dp)
                                )
                                .background(
                                    color = MaterialTheme.colorScheme.baseBackground,
                                )
                        ) {

                        }


                        AsyncImage(
                            request = ImageRequest(
                                context = context,
                                uri = userDetailData.avatar,
                                configBlock = configBlock,
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .border(
                                    border = BorderStroke(
                                        2.dp,
                                        MaterialTheme.colorScheme.background
                                    ),
                                    shape = CircleShape
                                )

                        )

                    }

                }


            }

        }


    }
}