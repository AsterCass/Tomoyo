import androidx.compose.ui.window.ComposeUIViewController
import constant.enums.MainNavigationEnum
import data.PlatformInitData
import di.KoinInit

fun MainViewController() = ComposeUIViewController(
    configure = {
        KoinInit().init {
            modules()
        }
    }
) { MainApp(
    platformData = PlatformInitData(
        extraNavigationList = listOf(
            MainNavigationEnum.ARTICLES,
            MainNavigationEnum.Contacts,
            MainNavigationEnum.MUSICS,
            MainNavigationEnum.SETTING,
        )
    )
) }