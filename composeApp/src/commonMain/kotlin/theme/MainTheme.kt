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

    primary = Color(0XFF379B9B),
    onPrimary = Color(0xFFE9E9E9),
    inversePrimary = Color(0XFF96C8C8),

    secondary = Color.Red,
    onSecondary = Color.Blue,
    onSecondaryContainer = Color(0XFF379B9B),
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

        )
}