package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val RoyalDarkColorScheme = darkColorScheme(
    primary = RoyalGold,
    onPrimary = MidnightBlack,
    primaryContainer = RoyalGoldDeep,
    onPrimaryContainer = RoyalGoldLight,
    secondary = RoyalGoldLight,
    onSecondary = MidnightBlack,
    secondaryContainer = MidnightSurfaceElevated,
    onSecondaryContainer = RoyalGoldBright,
    tertiary = RoyalPurple,
    onTertiary = Color.White,
    background = MidnightBlack,
    onBackground = TextPrimary,
    surface = MidnightSurface,
    onSurface = TextPrimary,
    surfaceVariant = MidnightSurfaceElevated,
    onSurfaceVariant = TextSecondary,
    outline = RoyalGold.copy(alpha = 0.35f),
    outlineVariant = MidnightCardBorder,
    error = RoyalError,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = RoyalDarkColorScheme,
        typography = Typography,
        content = content
    )
}
