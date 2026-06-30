package com.bingo.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightScheme = lightColorScheme(
    primary = AppColors.PrimaryOrange,
    secondary = AppColors.GrowthGreen,
    tertiary = AppColors.EnergyYellow,
    background = AppColors.Background,
    surface = AppColors.CardWhite,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = AppColors.TextNavy,
    onSurface = AppColors.TextNavy
)

@Composable
fun BingoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightScheme,
        typography = MaterialTheme.typography,
        content = content
    )
}
