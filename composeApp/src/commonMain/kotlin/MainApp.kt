import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import api.BaseApi
import constant.enums.MainNavigationEnum
import data.ArticleSimpleModel
import data.ChatRowModel
import data.PlatformInitData
import data.UserDataModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.subscribeText
import org.hildan.krossbow.websocket.sockjs.SockJSClient
import ui.components.MainAppBar
import ui.components.MainAppNavigationBar
import ui.pages.MainArticleScreen
import ui.pages.MainChatScreen
import ui.pages.MainHomeScreen
import ui.pages.MainMusicsScreen
import ui.pages.MainSettingsScreen
import ui.pages.MainVideosScreen


@Composable
fun MainApp(
    platformData: PlatformInitData = PlatformInitData(),
    updatePlatformData: (PlatformInitData) -> Unit = {},
    navController: NavHostController = rememberNavController(),
) {
    MaterialTheme {

        println("reload MaterialTheme")


        //coroutine
        val apiCoroutine = rememberCoroutineScope()
//        val windowsCoroutine = rememberCoroutineScope()

        //navigation
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentScreen = MainNavigationEnum.valueOf(
            backStackEntry?.destination?.route ?: MainNavigationEnum.HOME.name
        )

        //socket
        val socketClient = remember { socketClient() }
        var socketSession by remember { mutableStateOf<StompSession?>(null) }

        //article data
        var articleDataList by remember { mutableStateOf(emptyList<ArticleSimpleModel>()) }

        //userData
        var userData by remember { mutableStateOf(UserDataModel()) }
        var userDataVersion by remember { mutableStateOf(0) }
        var lastUserToken: String? by remember { mutableStateOf(null) }

        //update session
        socketSessionUpdate(
//            windowsCoroutine,
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


//        CoroutineScope(Dispatchers.IO).launch {
////        if (null == socketSession) {
//            println("====================build socket====================")
//            try {
//                val client = StompClient(SockJSClient())
//                val session: StompSession = client.connect(
//                    "https://api.astercasc.com/yui/chat-websocket/socketAuthNoError?User-Token=${userData.token}"
//                )
//                socketSession = session
//                val subscription: Flow<String> =
//                    session.subscribeText("/user/${userData.token}/message/receive")
//                val collectorJob = apiCoroutine.launch {
//                    subscription.collect { msg ->
//                        val chatRow: ChatRowModel = Json {
//                            prettyPrint = true
//                            isLenient = true
//                            ignoreUnknownKeys = true
//                        }.decodeFromString(msg)
//                        userData.chatId = chatRow.fromChatId
//                        userData.chatRowList += chatRow
//                        println(userData.chatRowList)
//                    }
//                }
//
//            } catch (ex: Exception) {
//                ex.printStackTrace()
//            }
//        }


        Scaffold(
            topBar = {
                MainAppBar(
                    currentScreen = currentScreen,
                )
            },
            bottomBar = {
                MainAppNavigationBar(
                    currentScreen = currentScreen,
                    navigationClicked = { navObj -> navController.navigate(navObj.name) },
                    extraNavigationList = platformData.extraNavigationList,
                )
            }
        ) { innerPadding ->

            NavHost(
                navController = navController,
                startDestination = MainNavigationEnum.HOME.name,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding)
            ) {

                println("reload NavHost")

                composable(route = MainNavigationEnum.HOME.name) {
                    MainHomeScreen(
                        modifier = Modifier.fillMaxHeight()
                    )
                }

                if (platformData.extraNavigationList.contains(MainNavigationEnum.ARTICLES)) {
                    composable(route = MainNavigationEnum.ARTICLES.name) {
                        MainArticleScreen(
                            articleDataList = articleDataList,
                            modifier = Modifier.fillMaxHeight()
                        ) {
                            apiCoroutine.launch {
                                articleDataList = articleDataList + BaseApi().getArticleList(
                                    offset = articleDataList.size,
                                )
                            }
                        }
                    }
                }

                if (platformData.extraNavigationList.contains(MainNavigationEnum.MUSICS)) {
                    composable(route = MainNavigationEnum.MUSICS.name) {
                        MainMusicsScreen(
                            modifier = Modifier.fillMaxHeight()
                        )
                    }
                }

                if (platformData.extraNavigationList.contains(MainNavigationEnum.CHAT)) {
                    composable(route = MainNavigationEnum.CHAT.name) {
                        MainChatScreen(
                            userData = userData,
                            userDataVersion = userDataVersion,
                            socketSession = socketSession,
                            modifier = Modifier.fillMaxHeight()
                        )
                    }
                }

                if (platformData.extraNavigationList.contains(MainNavigationEnum.VIDEOS)) {
                    composable(route = MainNavigationEnum.VIDEOS.name) {
                        MainVideosScreen(
                            modifier = Modifier.fillMaxHeight()
                        )
                    }
                }

                if (platformData.extraNavigationList.contains(MainNavigationEnum.SETTING)) {
                    composable(route = MainNavigationEnum.SETTING.name) {
                        MainSettingsScreen(
                            userData = userData,
                            login = { account: String, passwd: String ->
                                apiCoroutine.launch {
                                    userData = BaseApi().login(account, passwd)
                                }
                            },
                            modifier = Modifier.fillMaxHeight(),
                        )
                    }
                }

            }
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