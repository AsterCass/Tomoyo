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
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import constant.enums.MainNavigationEnum
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

            //navigator

            Navigator(MainHomeScreen) { navigator ->

                val tabList = listOf(
                    MainNavigationEnum.ARTICLES,
                    MainNavigationEnum.MUSICS,
                    MainNavigationEnum.HOME,
                    MainNavigationEnum.VIDEOS,
                    MainNavigationEnum.Contacts,
                    MainNavigationEnum.SETTING,
                )

                if (tabList.any { navigator.lastItem.key.startsWith(it.code) }) {
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
                                CurrentScreen()

                                //notification
                                Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                                    NotificationComponent()
                                }

                            }

                        }
                    )
                } else {
                    BoxWithConstraints(
                        modifier = Modifier.fillMaxSize()
                    ) {

                        //size
                        val constraints = this.constraints
                        mainModel.updateMainPageContainerConstraints(constraints)

                        //screen1
                        CurrentScreen()

                        //notification
                        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                            NotificationComponent()
                        }

                    }
                }

            }


            //navigation
//            val tabs = remember {
//                listOf(
//                    HomeTab, ArticlesTab, MusicsTab,
//                    VideosTab, ContactsTab, SettingTab
//                )
//            }
//            var lastTabIndex = HomeTab.options.index
//            TabNavigator(
//                tab = HomeTab,
//                tabDisposable = {
//                    TabDisposable(
//                        navigator = it,
//                        tabs = tabs
//                    )
//                },
//            ) { tabNavigator ->
//
//                Scaffold(
////                    contentWindowInsets = WindowInsets.navigationBars,
////                    modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
////                        .imePadding(),
//                    topBar = {
//                        MainAppBar()
//                    },
//                    bottomBar = {
//                        MainAppNavigationBar(
//                            extraNavigationList = platformData.extraNavigationList,
//                        )
//                    },
//                    content = { padding ->
//
//                        BoxWithConstraints(
//                            modifier = Modifier.padding(padding).fillMaxSize()
//                        ) {
//
//                            //size
//                            val constraints = this.constraints
//                            mainModel.updateMainPageContainerConstraints(constraints)
//
//                            //screen1
//                            CurrentTab()
//
//                            //screen2
////                            Crossfade(
////                                targetState = tabNavigator.current,
////                                animationSpec = tween(durationMillis = 1000)
////                            ) { selectedTab ->
////                                selectedTab.Content()
////                            }
//
//                            //screen3
////                            tabs.forEach { tab ->
////                                val isToRight = tabNavigator.current.options.index > lastTabIndex
////                                AnimatedVisibility(
////                                    visible = tabNavigator.current == tab,
////                                    enter = if (tab == HomeTab) fadeIn(
////                                        animationSpec = tween(
////                                            durationMillis = 800
////                                        )
////                                    )
////                                    else
////                                        slideInHorizontally(
////                                            initialOffsetX = { fullHeight ->
////                                                if (isToRight)
////                                                    fullHeight else -fullHeight
////                                            },
////                                            animationSpec = tween(
////                                                durationMillis = 800,
////                                            ),
////                                        ),
////                                    exit = fadeOut(animationSpec = tween(durationMillis = 400)),
////                                ) {
////                                    lastTabIndex = tab.options.index
////                                    tab.Content()
////                                }
////                            }
//
//                            //notification
//                            Box(modifier = Modifier.align(Alignment.BottomCenter)) {
//                                NotificationComponent()
//                            }
//
//                        }
//
//                    }
//                )
//            }

        }
    }

}


//data class MainTabsScreen(
//    val platformData: PlatformInitData,
//    val mainModel: MainScreenModel
//) : Screen {
//    @Composable
//    override fun Content() {
//
//        //navigation
//        val tabs = remember {
//            listOf(
//                HomeTab, ArticlesTab, MusicsTab,
//                VideosTab, ContactsTab, SettingTab
//            )
//        }
//        TabNavigator(
//            tab = HomeTab,
//            tabDisposable = {
//                TabDisposable(
//                    navigator = it,
//                    tabs = tabs
//                )
//            },
//        ) { nav ->
//
//
//            Scaffold(
//                topBar = {
//                    MainAppBar()
//                },
//                bottomBar = {
//                    MainAppNavigationBar(
//                        extraNavigationList = platformData.extraNavigationList,
//                    )
//                },
//                content = { padding ->
//
//                    BoxWithConstraints(
//                        modifier = Modifier.padding(padding).fillMaxSize()
//                    ) {
//
//                        //size
//                        val constraints = this.constraints
//                        mainModel.updateMainPageContainerConstraints(constraints)
//
//                        //screen1
////                        CurrentTab()
//                        SlideTransition(nav)
//
//                        //notification
//                        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
//                            NotificationComponent()
//                        }
//
//                    }
//
//                }
//            )
//        }
//    }
//}