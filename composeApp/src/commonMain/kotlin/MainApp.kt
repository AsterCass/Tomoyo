import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import api.baseJsonConf
import biz.StatusBar
import biz.TabTransition
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import data.PlatformInitData
import data.UserDataModel
import data.model.MainScreenModel
import data.store.DataStorageManager
import kotlinx.coroutines.launch
import org.koin.compose.KoinContext
import org.koin.compose.koinInject
import theme.LightColorScheme
import theme.MainTypography
import ui.components.InitForNoComposableRes
import ui.components.MainAppBar
import ui.components.MainAppNavigationBar
import ui.components.NotificationComponent
import ui.pages.MainHomeScreen

@Composable
fun MainApp(
    platformData: PlatformInitData = PlatformInitData(),
    mainModel: MainScreenModel = koinInject(),
    dataStorageManager: DataStorageManager = koinInject(),
) {
    //data
    val firstTryLinkSocket = mainModel.firstTryLinkSocket.value
    val userDataStringDb = dataStorageManager.getNonFlowString(DataStorageManager.USER_DATA)

    //coroutine
    val commonApiCoroutine = rememberCoroutineScope()

    KoinContext {
        MaterialTheme(
            colorScheme = LightColorScheme,
            typography = MainTypography(),
        ) {
            //init
            StatusBar().updateColor(MaterialTheme.colorScheme.surface, true)
            InitForNoComposableRes()

            //user status
            if (userDataStringDb.isNotBlank() && firstTryLinkSocket) {
                mainModel.triedLinkSocket()
                val userDataDb: UserDataModel = baseJsonConf.decodeFromString(userDataStringDb)
                if (!userDataDb.token.isNullOrBlank()) {
                    commonApiCoroutine.launch {
                        mainModel.login(
                            dbData = userDataDb,
                            forceLogin = true
                        )
                    }
                }
            }


            //navigation
            Navigator(MainTabsScreen(platformData, mainModel)) { navigator ->
                SlideTransition(navigator)
            }

        }
    }

}


data class MainTabsScreen(
    val platformData: PlatformInitData,
    val mainModel: MainScreenModel
) : Screen {
    @Composable
    override fun Content() {
        //navigator
        Navigator(MainHomeScreen) { navigator ->
            Scaffold(
                topBar = {
                    MainAppBar()
                },
                bottomBar = {
                    MainAppNavigationBar(
                        extraNavigationList = platformData.extraNavigationList,
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