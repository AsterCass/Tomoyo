import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import biz.StatusBar
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabDisposable
import cafe.adriel.voyager.navigator.tab.TabNavigator
import data.PlatformInitData
import data.model.MainScreenModel
import org.koin.compose.KoinContext
import org.koin.compose.koinInject
import theme.LightColorScheme
import theme.MainTypography
import ui.components.ArticlesTab
import ui.components.ChatTab
import ui.components.HomeTab
import ui.components.InitForNoComposableRes
import ui.components.MainAppBar
import ui.components.MainAppNavigationBar
import ui.components.MusicsTab
import ui.components.NotificationComponent
import ui.components.SettingTab
import ui.components.VideosTab

@Composable
fun MainApp(
    platformData: PlatformInitData = PlatformInitData(),
    mainModel: MainScreenModel = koinInject(),
) {

    val showNavBar = mainModel.showNavBar.collectAsState().value

    KoinContext {
        MaterialTheme(
            colorScheme = LightColorScheme,
            typography = MainTypography(),
        ) {
            //init
            StatusBar().updateColor(MaterialTheme.colorScheme.background, true)
            InitForNoComposableRes()

            //navigation
            TabNavigator(
                tab = HomeTab,
                tabDisposable = {
                    TabDisposable(
                        navigator = it,
                        tabs = listOf(
                            HomeTab, ArticlesTab, MusicsTab, VideosTab, ChatTab, SettingTab
                        )
                    )
                },
            ) { _ ->

                Scaffold(
                    topBar = {
                        AnimatedVisibility(
                            visible = showNavBar,
                            enter = fadeIn(animationSpec = tween(durationMillis = 500)) +
                                    slideInVertically(
                                        initialOffsetY = { -it },
                                        animationSpec = tween(durationMillis = 500)
                                    ),
                            exit = fadeOut(animationSpec = tween(durationMillis = 500)) +
                                    slideOutVertically(
                                        targetOffsetY = { -it },
                                        animationSpec = tween(durationMillis = 500)
                                    ),
                        ) {
                            MainAppBar()
                        }

                    },
                    bottomBar = {
                        AnimatedVisibility(
                            visible = showNavBar,
                            enter = fadeIn(animationSpec = tween(durationMillis = 500)) +
                                    slideInVertically(
                                        initialOffsetY = { it },
                                        animationSpec = tween(durationMillis = 500)
                                    ),
                            exit = fadeOut(animationSpec = tween(durationMillis = 500)) +
                                    slideOutVertically(
                                        targetOffsetY = { it },
                                        animationSpec = tween(durationMillis = 500)
                                    ),
                        ) {
                            MainAppNavigationBar(
                                extraNavigationList = platformData.extraNavigationList,
                            )
                        }

                    },
                    content = { padding ->

                        BoxWithConstraints(
                            modifier = Modifier.padding(padding).fillMaxSize()
                        ) {

                            //size
                            val constraints = this.constraints
                            mainModel.updateMainPageContainerConstraints(constraints)

                            //screen
                            CurrentTab()

                            //notification
                            Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                                NotificationComponent()
                            }

                        }

                    }
                )
            }

        }
    }

}