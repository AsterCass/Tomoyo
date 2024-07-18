import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import data.PlatformInitData
import data.UserDataModel
import kotlinx.coroutines.launch
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

        //coroutine
        val apiCoroutine = rememberCoroutineScope()

        //navigation
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentScreen = MainNavigationEnum.valueOf(
            backStackEntry?.destination?.route ?: MainNavigationEnum.HOME.name
        )
        //article data
        var articleDataList by remember { mutableStateOf(emptyList<ArticleSimpleModel>()) }

        //userData
        var userData by remember { mutableStateOf(UserDataModel()) }

        println("reload MaterialTheme")

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
                            LaunchedEffect(Unit) {
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
                            platformData = platformData,
                            updatePlatformData = updatePlatformData,
                            modifier = Modifier.fillMaxHeight(),
                        )
                    }
                }

            }
        }


    }
}