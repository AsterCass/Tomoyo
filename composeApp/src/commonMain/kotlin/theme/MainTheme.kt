package theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import tomoyo.composeapp.generated.resources.Res
import tomoyo.composeapp.generated.resources.RobotoSlab_VariableFont_wght

val androidx.compose.material3.ColorScheme.unselectedColor: Color
    get() = if (this.background == LightColorScheme.background)
        Color(0XFFc8c8c8) else Color.Transparent

val androidx.compose.material3.ColorScheme.subTextColor: Color
    get() = if (this.background == LightColorScheme.background)
        Color(0XFF979797) else Color.Transparent

val androidx.compose.material3.ColorScheme.deepIconColor: Color
    get() = if (this.background == LightColorScheme.background)
        Color(0XFF333333) else Color.Transparent

val androidx.compose.material3.ColorScheme.third: Color
    get() = if (this.background == LightColorScheme.background)
        Color((0XFFF26E56)) else Color.Transparent

val androidx.compose.material3.ColorScheme.onThird: Color
    get() = if (this.background == LightColorScheme.background)
        Color((0xFFE9E9E9)) else Color.Transparent

val androidx.compose.material3.ColorScheme.inverseThird: Color
    get() = if (this.background == LightColorScheme.background)
        Color((0XFFf6b5a9)) else Color.Transparent

val androidx.compose.material3.ColorScheme.baseBackground: Color
    get() = if (this.background == LightColorScheme.background)
        Color(0XE0FFFFFF) else Color.Black

val androidx.compose.material3.ColorScheme.baseBackgroundBlack: Color
    get() = if (this.background == LightColorScheme.background)
        Color(0XE0222222) else Color.Black

val androidx.compose.material3.ColorScheme.halfTransSurfaceVariant: Color
    get() = if (this.background == LightColorScheme.background)
        Color(0XE0E0E0E0) else Color.Black

val androidx.compose.material3.ColorScheme.pureColor: Color
    get() = if (this.background == LightColorScheme.background)
        Color.White else Color.Black




val DarkColorScheme = darkColorScheme(
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White
)

val LightColorScheme = lightColorScheme(
    background = Color(0xFFEBEBEB),
    onBackground = Color(0XFF191919),

    surface = Color(0xFFF2F2F2),
    onSurface = Color(0XFF191919),

    surfaceVariant = Color(0XFFe0e0e0),
    onSurfaceVariant = Color(0XFF191919),
    surfaceContainer = Color.Transparent,

    primary = Color(0XFF689f38),
    onPrimary = Color(0xFFE9E9E9),
    inversePrimary = Color(0XFFb1ce9a),

    secondary = Color(0XFF0378BD),
    onSecondary = Color(0xFFE9E9E9),
    onSecondaryContainer = Color(0XFF689f38),
    secondaryContainer = Color.Transparent,


//    surfaceContainer = Color.Transparent,
//    surfaceContainerLow = Color.Transparent,
//    surfaceContainerHigh = Color.Transparent,
//    surfaceContainerLowest = Color.Transparent,
//    surfaceContainerHighest = Color.Transparent,
//    surfaceVariant = Color.Transparent,

//    surfaceBright = Color.Transparent,
//    surfaceDim = Color.Transparent,
//    inverseOnSurface = Color.Transparent,
//
//
//    inverseSurface = Color.Cyan,
//
//    primaryContainer = Color.Red,
//    onPrimaryContainer = Color.Black,
//
//
//
//    secondary = Color.Yellow,
//    onSecondary = Color.Black,
//
//
//    error = Color(0xFFB00020),
//    onError = Color.White,
//    errorContainer = Color.White,
//    onErrorContainer = Color.Black,
//
//    outline = Color(0xFF79747E),
//    outlineVariant = Color(0xFFCCC2DC),
//
//    surfaceTint = Color.Transparent,
//    scrim = Color.Black,
)


@Composable
fun MainTypography(): Typography {

    val defaultFontFamily = FontFamily(
        org.jetbrains.compose.resources.Font(
            Res.font.RobotoSlab_VariableFont_wght
        )
    )

    return Typography(
        labelSmall = MaterialTheme.typography.labelSmall.copy(
            fontFamily = defaultFontFamily,
        ),
        labelMedium = MaterialTheme.typography.labelMedium.copy(
            fontFamily = defaultFontFamily,
        ),
        labelLarge = MaterialTheme.typography.labelLarge.copy(
            fontFamily = defaultFontFamily,
            //fontWeight = FontWeight.Bold,
        ),
        bodySmall = MaterialTheme.typography.bodySmall.copy(
            fontFamily = defaultFontFamily,
        ),
        bodyMedium = MaterialTheme.typography.bodyMedium.copy(
            fontFamily = defaultFontFamily,
        ),
        bodyLarge = MaterialTheme.typography.bodyLarge.copy(
            fontFamily = defaultFontFamily,
            //fontWeight = FontWeight.Bold,
        ),
        titleSmall = MaterialTheme.typography.titleSmall.copy(
            fontFamily = defaultFontFamily,
        ),
        titleMedium = MaterialTheme.typography.titleMedium.copy(
            fontFamily = defaultFontFamily,
        ),
        titleLarge = MaterialTheme.typography.titleLarge.copy(
            fontFamily = defaultFontFamily,
        ),
        headlineSmall = MaterialTheme.typography.headlineSmall.copy(
            fontFamily = defaultFontFamily,
        ),
        headlineMedium = MaterialTheme.typography.headlineMedium.copy(
            fontFamily = defaultFontFamily,
        ),
        headlineLarge = MaterialTheme.typography.headlineLarge.copy(
            fontFamily = defaultFontFamily,
        ),


        )
}