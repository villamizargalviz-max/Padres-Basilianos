package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

fun getTypographyForTheme(theme: BasilianAppTheme): Typography {
    // Títulos en script manuscrita (Cursive) y explicaciones en tipografía sin serifas (SansSerif)
    val scriptFont = FontFamily.Cursive
    val sansSerifFont = FontFamily.SansSerif

    return Typography(
        displayLarge = TextStyle(
            fontFamily = scriptFont,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            lineHeight = 40.sp,
            letterSpacing = 0.sp
        ),
        displayMedium = TextStyle(
            fontFamily = scriptFont,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            lineHeight = 36.sp,
            letterSpacing = 0.sp
        ),
        displaySmall = TextStyle(
            fontFamily = scriptFont,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            lineHeight = 32.sp,
            letterSpacing = 0.sp
        ),
        headlineLarge = TextStyle(
            fontFamily = scriptFont,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            lineHeight = 32.sp,
            letterSpacing = 0.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = scriptFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.sp
        ),
        headlineSmall = TextStyle(
            fontFamily = scriptFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = 19.sp,
            lineHeight = 25.sp,
            letterSpacing = 0.sp
        ),
        titleLarge = TextStyle(
            fontFamily = scriptFont,
            fontWeight = FontWeight.Bold,
            fontSize = 21.sp,
            lineHeight = 27.sp,
            letterSpacing = 0.sp
        ),
        titleMedium = TextStyle(
            fontFamily = scriptFont,
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.15.sp
        ),
        titleSmall = TextStyle(
            fontFamily = sansSerifFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = sansSerifFont,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = sansSerifFont,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.25.sp
        ),
        bodySmall = TextStyle(
            fontFamily = sansSerifFont,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.4.sp
        ),
        labelLarge = TextStyle(
            fontFamily = sansSerifFont,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp
        ),
        labelMedium = TextStyle(
            fontFamily = sansSerifFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        ),
        labelSmall = TextStyle(
            fontFamily = sansSerifFont,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        )
    )
}

// Default fallback typography
val Typography = getTypographyForTheme(BasilianAppTheme.ESCUDO_BASILIANO)
