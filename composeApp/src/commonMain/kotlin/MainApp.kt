import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import api.baseJsonConf
import biz.StatusBar
import biz.TabTransition
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.transitions.SlideTransition
import constant.enums.ViewEnum
import data.PlatformInitData
import data.UserDataModel
import data.model.ArticleScreenModel
import data.model.ContactScreenModel
import data.model.MainScreenModel
import data.model.MusicScreenModel
import data.store.DataStorageManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.KoinContext
import org.koin.compose.koinInject
import theme.LightColorScheme
import theme.MainTypography
import tomoyo.composeapp.generated.resources.Res
import tomoyo.composeapp.generated.resources.bg1
import ui.components.InitForNoComposableRes
import ui.components.MainAppBar
import ui.components.MainAppNavigationBar
import ui.components.NotificationComponent
import ui.pages.MainHomeScreen

@Composable
fun MainApp(
    platformData: PlatformInitData = PlatformInitData(),
) {
    KoinContext {
        MaterialTheme(
            colorScheme = LightColorScheme,
            typography = MainTypography(),
        ) {
            val mainModel: MainScreenModel = koinInject()
            mainModel.initPlatformInitData(platformData)
            //navigation
            Navigator(PreLoadScreen()) { navigator ->
                SlideTransition(navigator)
            }

        }
    }

}

class PreLoadScreen : Screen {

    override val key: ScreenKey = "${ViewEnum.PRE_LOAD.code}$uniqueScreenKey"

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        // Model Inject
        val dataStorageManager: DataStorageManager = koinInject()
        val mainModel: MainScreenModel = koinInject()
        val articleModel: ArticleScreenModel = koinInject()
        val musicModel: MusicScreenModel = koinInject()
        val contactModel: ContactScreenModel = koinInject()

        // Custom data inject
        StatusBar().UpdateColor(Color.Transparent, Color.Transparent, false)
        InitForNoComposableRes()

        // Coroutine
        val preloadCoroutine = rememberCoroutineScope()

        // Navigation
        val navigator = LocalNavigator.currentOrThrow

        preloadCoroutine.launch {
            // Data
            val firstTryLinkSocket = mainModel.firstTryLinkSocket.value
            val userDataStringDb = dataStorageManager.getNonFlowString(DataStorageManager.USER_DATA)

            // User Status
            if (userDataStringDb.isNotBlank() && firstTryLinkSocket) {
                mainModel.triedLinkSocket()
                val userDataDb: UserDataModel = baseJsonConf.decodeFromString(userDataStringDb)
                if (!userDataDb.token.isNullOrBlank()) {
                    preloadCoroutine.launch {
                        mainModel.login(
                            dbData = userDataDb,
                            forceLogin = true
                        )
                    }
                }
            }

            // Data pre load
            articleModel.updateArticleList()
            musicModel.updateAllAudioList()
            contactModel.loadPublicUser()

            // Init finish
            delay(500)
            navigator.replace(MainTabsScreen())
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(Res.drawable.bg1),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        }

    }
}

class MainTabsScreen : Screen {
    @Composable
    override fun Content() {

        // Model Inject
        val mainModel: MainScreenModel = koinInject()

        // Reset color
        StatusBar().UpdateColor(
            Color.Transparent,
            MaterialTheme.colorScheme.surface,
            true,
        )

        //navigator
        Navigator(MainHomeScreen) { navigator ->
            Scaffold(
                topBar = {
                    MainAppBar()
                },
                bottomBar = {
                    MainAppNavigationBar(
                        extraNavigationList = mainModel.getPlatformInitData().extraNavigationList,
                    )
                },
                content = { padding ->

                    BoxWithConstraints(
                        modifier = Modifier.padding(padding).fillMaxSize()
                    ) {
                        //size
                        val constraints = this.constraints
                        mainModel.updateMainPageContainerConstraints(constraints)

                        //screen1
                        TabTransition(mainModel.getSecondLastNavKey(), navigator)
                        mainModel.updateSecondLastNavKey(navigator.lastItem.key)

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