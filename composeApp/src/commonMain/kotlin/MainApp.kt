import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabDisposable
import cafe.adriel.voyager.navigator.tab.TabNavigator
import data.ArticleSimpleModel
import data.ChatRowModel
import data.MusicSimpleModel
import data.PlatformInitData
import data.UserDataModel
import data.rememberMusicPlayerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.subscribeText
import org.hildan.krossbow.websocket.sockjs.SockJSClient
import org.koin.compose.KoinContext
import theme.LightColorScheme
import theme.MainTypography
import ui.components.ArticlesTab
import ui.components.AudioPlayer
import ui.components.HomeTab
import ui.components.InitForNoComposableRes
import ui.components.MainAppBar
import ui.components.MainAppNavigationBar
import ui.components.NotificationComponent


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainApp(
    platformData: PlatformInitData = PlatformInitData(),
    updatePlatformData: (PlatformInitData) -> Unit = {},
) {


    KoinContext {
        MaterialTheme(
            colorScheme = LightColorScheme,
            typography = MainTypography(),
        ) {

            println("reload MaterialTheme")

            //coroutine
            val apiCoroutine = rememberCoroutineScope()

            //navigation
//        val backStackEntry by navController.currentBackStackEntryAsState()
//        val currentScreen = MainNavigationEnum.valueOf(
//            backStackEntry?.destination?.route ?: MainNavigationEnum.HOME.name
//        )

            //socket
            val socketClient = remember { socketClient() }
            var socketSession by remember { mutableStateOf<StompSession?>(null) }

            //article data
            var articleDataList by remember { mutableStateOf(emptyList<ArticleSimpleModel>()) }

            //music data
            val playerState = rememberMusicPlayerState()
            val player = remember { AudioPlayer(playerState) }
            var musicDataList by remember { mutableStateOf(emptyList<MusicSimpleModel>()) }

            if (musicDataList.isEmpty()) {
                musicDataList = listOf(
                    MusicSimpleModel(
                        id = "1",
                        musicName = "歌曲1",
                        musicAuthor = "张三1",
                        musicUrl = "https://astercasc-web-admin-1256368017.cos.ap-shanghai.myqcloud.com/test/1.m4a",
                    ),
                    MusicSimpleModel(
                        id = "2",
                        musicName = "歌曲2",
                        musicAuthor = "张三2",
                        musicUrl = "https://astercasc-web-admin-1256368017.cos.ap-shanghai.myqcloud.com/test/2.m4a",
                    ),
                    MusicSimpleModel(
                        id = "3",
                        musicName = "歌曲3",
                        musicAuthor = "张三3",
                        musicUrl = "https://astercasc-web-admin-1256368017.cos.ap-shanghai.myqcloud.com/test/1.mp3",
                    ),
                    MusicSimpleModel(
                        id = "3",
                        musicName = "歌曲3",
                        musicAuthor = "张三3",
                        musicUrl = "https://astercasc-web-admin-1256368017.cos.ap-shanghai.myqcloud.com/test/1.mp3",
                    ),
                    MusicSimpleModel(
                        id = "3",
                        musicName = "歌曲3",
                        musicAuthor = "张三3",
                        musicUrl = "https://astercasc-web-admin-1256368017.cos.ap-shanghai.myqcloud.com/test/1.mp3",
                    ),
                    MusicSimpleModel(
                        id = "3",
                        musicName = "歌曲3",
                        musicAuthor = "张三3",
                        musicUrl = "https://astercasc-web-admin-1256368017.cos.ap-shanghai.myqcloud.com/test/1.mp3",
                    ),
                    MusicSimpleModel(
                        id = "3",
                        musicName = "歌曲3",
                        musicAuthor = "张三3",
                        musicUrl = "https://astercasc-web-admin-1256368017.cos.ap-shanghai.myqcloud.com/test/1.mp3",
                    ),
                    MusicSimpleModel(
                        id = "3",
                        musicName = "歌曲3",
                        musicAuthor = "张三3",
                        musicUrl = "https://astercasc-web-admin-1256368017.cos.ap-shanghai.myqcloud.com/test/1.mp3",
                    ),
                    MusicSimpleModel(
                        id = "3",
                        musicName = "歌曲3",
                        musicAuthor = "张三3",
                        musicUrl = "https://astercasc-web-admin-1256368017.cos.ap-shanghai.myqcloud.com/test/1.mp3",
                    ),
                    MusicSimpleModel(
                        id = "3",
                        musicName = "歌曲3",
                        musicAuthor = "张三3",
                        musicUrl = "https://astercasc-web-admin-1256368017.cos.ap-shanghai.myqcloud.com/test/1.mp3",
                    ),
                )
            }


            //userData
            var userData by remember { mutableStateOf(UserDataModel()) }
            var userDataVersion by remember { mutableStateOf(0) }
            var lastUserToken: String? by remember { mutableStateOf(null) }

            //update session
            socketSessionUpdate(
                socketClient,
                socketSession,
                userData,
                lastUserToken,
                updateSession = { newSession, newToken ->
                    socketSession = newSession
                    lastUserToken = newToken
                },
                updateUserVersion = { userDataVersion++ }
            )

            println("reload Main")

            //init
            InitForNoComposableRes()

            println("reload Main xxxxxxxxxx")

            TabNavigator(
                tab = HomeTab,
                tabDisposable = {
                    TabDisposable(
                        navigator = it,
                        tabs = listOf(HomeTab, ArticlesTab)
                    )
                }
            ) { _ ->

                Scaffold(
                    topBar = {
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(animationSpec = tween(durationMillis = 750)) +
                                    slideInVertically(
                                        initialOffsetY = { -it },
                                        animationSpec = tween(durationMillis = 750)
                                    ),
                            exit = fadeOut(animationSpec = tween(durationMillis = 750)) +
                                    slideOutVertically(
                                        targetOffsetY = { -it },
                                        animationSpec = tween(durationMillis = 750)
                                    ),
                        ) {
                            MainAppBar()
                        }

                    },
                    bottomBar = {
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(animationSpec = tween(durationMillis = 750)) +
                                    slideInVertically(
                                        initialOffsetY = { it },
                                        animationSpec = tween(durationMillis = 750)
                                    ),
                            exit = fadeOut(animationSpec = tween(durationMillis = 750)) +
                                    slideOutVertically(
                                        targetOffsetY = { it },
                                        animationSpec = tween(durationMillis = 750)
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

                            val constraints = this.constraints

                            CurrentTab()

                            Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                                NotificationComponent()
                            }

                        }

                    }
                )


            }

//        //navigation graph
//        val navGraph = navController.createGraph(startDestination = MainNavigationEnum.HOME.name)
//        {
//
//            composable(
//                route = MainNavigationEnum.HOME.name,
//                enterTransition = { fadeIn(animationSpec = tween(durationMillis = 500)) },
//                exitTransition = { fadeOut(animationSpec = tween(durationMillis = 500)) },
//            ) {
//                MainPageContainerScreen {
//                    MainHomeScreen(
//                    )
//                }
//            }
//
//            if (platformData.extraNavigationList.contains(MainNavigationEnum.ARTICLES)) {
//                composable(
//                    route = MainNavigationEnum.ARTICLES.name,
//                    enterTransition = { fadeIn(animationSpec = tween(durationMillis = 500)) },
//                    exitTransition = { fadeOut(animationSpec = tween(durationMillis = 500)) },
//                ) {
//                    MainPageContainerScreen { constraints ->
//                        MainArticleScreen(
//                            constraints = constraints,
//                            articleDataList = articleDataList,
//                        ) {
//                            apiCoroutine.launch {
//                                articleDataList = articleDataList + BaseApi().getArticleList(
//                                    offset = articleDataList.size,
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//
//            if (platformData.extraNavigationList.contains(MainNavigationEnum.MUSICS)) {
//                composable(
//                    route = MainNavigationEnum.MUSICS.name,
//                    enterTransition = { fadeIn(animationSpec = tween(durationMillis = 500)) },
//                    exitTransition = { fadeOut(animationSpec = tween(durationMillis = 500)) },
//                ) {
//                    MainPageContainerScreen { constraints ->
//                        MainMusicsScreen(
//                            constraints = constraints,
//                            playList = musicDataList,
//                            currentTime = playerState.currentTime,
//                            totalDuration = playerState.totalDuration,
//                            isPlaying = playerState.isPlaying,
//                            onStart = { url ->
//                                player.start(url)
//                            },
//                            onPlay = {
//                                player.play()
////                                navController.navigate(MainNavigationEnum.MUSIC_PLAYER.name)
//                            },
//                            onPause = {
//                                player.pause()
//                            },
//                        )
//                    }
//                }
//            }
//
//            if (platformData.extraNavigationList.contains(MainNavigationEnum.CHAT)) {
//                composable(
//                    route = MainNavigationEnum.CHAT.name,
//                    enterTransition = { fadeIn(animationSpec = tween(durationMillis = 500)) },
//                    exitTransition = { fadeOut(animationSpec = tween(durationMillis = 500)) },
//                ) {
//                    MainPageContainerScreen {
//                        MainChatScreen(
//                            userData = userData,
//                            userDataVersion = userDataVersion,
//                            socketSession = socketSession,
//                        )
//                    }
//                }
//            }
//
//            if (platformData.extraNavigationList.contains(MainNavigationEnum.VIDEOS)) {
//                composable(
//                    route = MainNavigationEnum.VIDEOS.name,
//                    enterTransition = { fadeIn(animationSpec = tween(durationMillis = 500)) },
//                    exitTransition = { fadeOut(animationSpec = tween(durationMillis = 500)) },
//                ) {
//                    MainPageContainerScreen {
//                        MainVideosScreen(
//                        )
//                    }
//                }
//            }
//
//            if (platformData.extraNavigationList.contains(MainNavigationEnum.SETTING)) {
//                composable(
//                    route = MainNavigationEnum.SETTING.name,
//                    enterTransition = { fadeIn() },
//                    exitTransition = { fadeOut() },
//                ) {
//                    MainPageContainerScreen {
//                        MainSettingsScreen(
//                            userData = userData,
//                            login = { account: String, passwd: String ->
//                                apiCoroutine.launch {
//                                    userData = BaseApi().login(account, passwd)
//                                }
//                            },
//                        )
//
//                    }
//                }
//            }
//
//            composable(
//                route = MainNavigationEnum.MUSIC_PLAYER.name,
//                enterTransition = {
//                    fadeIn(animationSpec = tween(durationMillis = 1000)) + scaleIn(
//                        initialScale = 0.2f,
//                        animationSpec = tween(durationMillis = 1000),
//                    )
//                },
//                exitTransition = {
//                    fadeOut(animationSpec = tween(durationMillis = 1000)) + scaleOut(
//                        targetScale = 0.2f,
//                        animationSpec = tween(durationMillis = 1000),
//                    )
//                },
//            ) {
//                Box(modifier = Modifier.fillMaxSize().background(Color.Gray))
//            }
//
//        }

//        //main
//        Scaffold(
//            topBar = {
//                AnimatedVisibility(
//                    visible = !FULL_SCREEN_ROUTES.contains(navController.currentDestination?.route),
//                    enter = fadeIn(animationSpec = tween(durationMillis = 750)) +
//                            slideInVertically(
//                                initialOffsetY = { -it },
//                                animationSpec = tween(durationMillis = 750)
//                            ),
//                    exit = fadeOut(animationSpec = tween(durationMillis = 750)) +
//                            slideOutVertically(
//                                targetOffsetY = { -it },
//                                animationSpec = tween(durationMillis = 750)
//                            ),
//                ) {
//                    MainAppBar(
//                        currentScreen = currentScreen,
//                    )
//                }
//
//            },
//            bottomBar = {
//                AnimatedVisibility(
//                    visible = !FULL_SCREEN_ROUTES.contains(navController.currentDestination?.route),
//                    enter = fadeIn(animationSpec = tween(durationMillis = 750)) +
//                            slideInVertically(
//                                initialOffsetY = { it },
//                                animationSpec = tween(durationMillis = 750)
//                            ),
//                    exit = fadeOut(animationSpec = tween(durationMillis = 750)) +
//                            slideOutVertically(
//                                targetOffsetY = { it },
//                                animationSpec = tween(durationMillis = 750)
//                            ),
//                ) {
//                    MainAppNavigationBar(
//                        currentScreen = currentScreen,
//                        navigationClicked = { navObj -> navController.navigate(navObj.name) },
//                        extraNavigationList = platformData.extraNavigationList,
//                    )
//                }
//
//            },
//        ) { innerPadding ->
//
//            NavHost(
//                navController, navGraph,
//                modifier = Modifier
//                    .fillMaxSize()
//                    .verticalScroll(rememberScrollState())
//                    .padding(innerPadding)
//            )
//        }

        }
    }

}


private fun socketClient(): StompClient {
    println("reload socketClient")
    return StompClient(SockJSClient())
}

private fun socketSessionUpdate(
//    windowsCor: CoroutineScope,
    client: StompClient,
    session: StompSession?,
    userData: UserDataModel,
    lastUserToken: String?,
    updateSession: (StompSession?, String?) -> Unit,
    updateUserVersion: () -> Unit,
) {
    println(userData.token)
    println(lastUserToken)
    if (userData.token == lastUserToken) {
        return
    }
    println("update session")

    CoroutineScope(Dispatchers.IO).launch {
//    windowsCor.launch {
        println("update session start")
        session?.disconnect()
        if (null != userData.token) {
            val thisSession: StompSession = client.connect(
                "https://api.astercasc.com/yui/chat-websocket/socketAuthNoError?User-Token=${userData.token}"
            )
            val subscription: Flow<String> =
                thisSession.subscribeText("/user/${userData.token}/message/receive")
            updateSession(thisSession, userData.token)
            subscription.collect { msg ->
                val chatRow: ChatRowModel = Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                }.decodeFromString(msg)
                userData.chatId = chatRow.fromChatId
                userData.chatRowList += chatRow
                updateUserVersion()
                println(userData.chatRowList)
            }
        } else {
            updateSession(null, null)
        }
    }


}