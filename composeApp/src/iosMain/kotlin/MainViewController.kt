import androidx.compose.ui.window.ComposeUIViewController
import di.KoinInit

fun MainViewController() = ComposeUIViewController(
    configure = {
        KoinInit().init {
            modules()
        }
    }
) { MainApp() }