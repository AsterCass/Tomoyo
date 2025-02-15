package biz

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.aster.yuno.tomoyo.MainActivity

actual class StatusBar actual constructor() {
    @Composable
    actual fun UpdateColor(
        statusBarColor: Color,
        navigationBarColor: Color,
        isLight: Boolean,
    ) {

        MainActivity.mainContext as Activity
        val view = LocalView.current

        if (!view.isInEditMode) {
            SideEffect {
                val window = (view.context as Activity).window

                val insetsController = WindowCompat.getInsetsController(window, view)
                window.statusBarColor = statusBarColor.toArgb()
                window.navigationBarColor = navigationBarColor.toArgb()
                insetsController.isAppearanceLightStatusBars = isLight
                insetsController.isAppearanceLightNavigationBars = isLight
            }
        }





        //use enableEdgeToEdge() instead

//        val view = LocalView.current
//        if (!view.isInEditMode) {
//            SideEffect {
//                val window = (view.context as Activity).window
//                window.statusBarColor = bgColor.toArgb()
//                WindowCompat.getInsetsController(window, view)
//                    .isAppearanceLightStatusBars = textColorIsDark
//            }
//        }
    }
}