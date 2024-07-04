import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import constant.enums.MainNavigationEnum
import ui.components.MainAppBar
import ui.components.MainAppNavigationBar
import ui.pages.MainArticleScreen
import ui.pages.MainHomeScreen
import ui.pages.MainMusicsScreen
import ui.pages.MainSettingsScreen
import ui.pages.MainVideosScreen


@Composable
fun MainApp(
    navController: NavHostController = rememberNavController(),
) {
    MaterialTheme {

        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentScreen = MainNavigationEnum.valueOf(
            backStackEntry?.destination?.route ?: MainNavigationEnum.HOME.name
        )

        Scaffold(
            topBar = {
                MainAppBar(
                    currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { navController.navigateUp() }
                )
            },
            bottomBar = {
                MainAppNavigationBar(
                    currentScreen = currentScreen,
                    navigationClicked = { navObj -> navController.navigate(navObj.name) }
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
                composable(route = MainNavigationEnum.HOME.name) {
                    MainHomeScreen(
                        modifier = Modifier.fillMaxHeight()
                    )
                }
                composable(route = MainNavigationEnum.ARTICLES.name) {
                    MainArticleScreen(
                        modifier = Modifier.fillMaxHeight()
                    )
                }
                composable(route = MainNavigationEnum.MUSICS.name) {
                    MainMusicsScreen(
                        modifier = Modifier.fillMaxHeight()
                    )
                }
                composable(route = MainNavigationEnum.VIDEOS.name) {
                    MainVideosScreen(
                        modifier = Modifier.fillMaxHeight()
                    )
                }
                composable(route = MainNavigationEnum.SETTING.name) {
                    MainSettingsScreen(
                        modifier = Modifier.fillMaxHeight()
                    )
                }
            }
        }


    }
}