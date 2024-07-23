package theme

import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val androidx.compose.material3.ColorScheme.unselectedColor: Color
    get() = if (this.background == LightColorScheme.background)
        Color(0XFF979797) else Color.Transparent


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
    onSurfaceVariant = Color(0XFF191919),

    onSecondaryContainer = Color(0XFF191919),
    secondaryContainer = Color.Transparent,


//    surfaceContainer = Color.Transparent,
//    surfaceContainerLow = Color.Transparent,
//    surfaceContainerHigh = Color.Transparent,
//    surfaceContainerLowest = Color.Transparent,
//    surfaceContainerHighest = Color.Transparent,
//    surfaceVariant = Color.Transparent,
//
//    surfaceBright = Color.Transparent,
//    surfaceDim = Color.Transparent,
//    inverseOnSurface = Color.Transparent,
    inverseSurface = Color.Cyan,


    primary = Color(0xFF2D4836),
    onPrimary = Color(0xFFE9E9E9),


    primaryContainer = Color.Red,
    onPrimaryContainer = Color.Black,

    secondary = Color.Yellow,
    onSecondary = Color.Black,


    error = Color(0xFFB00020),
    onError = Color.White,
    errorContainer = Color.White,
    onErrorContainer = Color.Black,

    outline = Color(0xFF79747E),
    outlineVariant = Color(0xFFCCC2DC),

    surfaceTint = Color.Transparent,
    scrim = Color.Black,
)

val MainTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    )