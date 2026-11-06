package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = ShieldGreenLight,
    onPrimary = ShieldBlack,
    primaryContainer = ShieldGreenDark,
    onPrimaryContainer = Color.White,
    secondary = ShieldRedLight,
    onSecondary = ShieldBlack,
    secondaryContainer = ShieldRedDark,
    onSecondaryContainer = Color.White,
    tertiary = ShieldGoldLight,
    background = DarkCanvas,
    surface = DarkSurface,
    surfaceVariant = DarkCard,
    onBackground = DarkTextPrimary,
    onSurface = DarkTextPrimary,
    onSurfaceVariant = DarkTextSecondary
  )

private val LightColorScheme =
  lightColorScheme(
    primary = ShieldGreen,
    onPrimary = Color.White,
    primaryContainer = ShieldGreenContainer,
    onPrimaryContainer = ShieldGreenDark,
    secondary = ShieldRed,
    onSecondary = Color.White,
    secondaryContainer = ShieldRedContainer,
    onSecondaryContainer = ShieldRedDark,
    tertiary = ShieldGoldDark,
    background = ParchmentWhite,
    surface = Color.White,
    surfaceVariant = ParchmentCard,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark,
    onSurfaceVariant = TextSecondaryDark,
    outline = ParchmentBorder
  )

@Composable
fun MyApplicationTheme(
  appTheme: BasilianAppTheme = BasilianAppTheme.ESCUDO_BASILIANO,
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Use custom shield colors by default as requested
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val isDark = appTheme.isDark

  val activeColorScheme = if (isDark) {
    darkColorScheme(
      primary = appTheme.primaryColor,
      onPrimary = appTheme.onPrimaryColor,
      primaryContainer = appTheme.primaryColor.copy(alpha = 0.25f),
      onPrimaryContainer = appTheme.primaryColor,
      secondary = appTheme.secondaryColor,
      onSecondary = appTheme.onSecondaryColor,
      secondaryContainer = appTheme.secondaryColor.copy(alpha = 0.25f),
      onSecondaryContainer = appTheme.secondaryColor,
      tertiary = appTheme.goldColor,
      onTertiary = appTheme.onPrimaryColor,
      background = appTheme.backgroundColor,
      surface = appTheme.surfaceColor,
      surfaceVariant = appTheme.surfaceVariantColor,
      onBackground = appTheme.onSurfaceColor,
      onSurface = appTheme.onSurfaceColor,
      onSurfaceVariant = appTheme.onSurfaceVariantColor,
      outline = appTheme.outlineColor
    )
  } else {
    lightColorScheme(
      primary = appTheme.primaryColor,
      onPrimary = appTheme.onPrimaryColor,
      primaryContainer = appTheme.primaryColor.copy(alpha = 0.12f),
      onPrimaryContainer = appTheme.primaryColor,
      secondary = appTheme.secondaryColor,
      onSecondary = appTheme.onSecondaryColor,
      secondaryContainer = appTheme.secondaryColor.copy(alpha = 0.12f),
      onSecondaryContainer = appTheme.secondaryColor,
      tertiary = appTheme.goldColor,
      onTertiary = Color.White,
      background = appTheme.backgroundColor,
      surface = appTheme.surfaceColor,
      surfaceVariant = appTheme.surfaceVariantColor,
      onBackground = appTheme.onSurfaceColor,
      onSurface = appTheme.onSurfaceColor,
      onSurfaceVariant = appTheme.onSurfaceVariantColor,
      outline = appTheme.outlineColor
    )
  }

  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      else -> activeColorScheme
    }

  val dynamicTypography = getTypographyForTheme(appTheme)
  val dynamicShapes = androidx.compose.material3.Shapes(
    small = appTheme.cardShape,
    medium = appTheme.cardShape,
    large = appTheme.cardShape
  )

  MaterialTheme(
    colorScheme = colorScheme,
    typography = dynamicTypography,
    shapes = dynamicShapes,
    content = content
  )
}
