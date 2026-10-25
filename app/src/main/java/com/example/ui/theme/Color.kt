package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Midnight Black tones
val MidnightBlack = Color(0xFF08070B)
val MidnightBlackDeep = Color(0xFF050508)
val MidnightSurface = Color(0xFF121118)
val MidnightSurfaceElevated = Color(0xFF181722)
val MidnightCardBorder = Color(0x33D4AF37) // 20% alpha gold border

// Metallic Royal Gold tones
val RoyalGold = Color(0xFFD4AF37)
val RoyalGoldLight = Color(0xFFFFDF73)
val RoyalGoldBright = Color(0xFFFFF2A8)
val RoyalGoldDark = Color(0xFF9E7B26)
val RoyalGoldDeep = Color(0xFF6B5115)

// Status / Accents
val RoyalSuccess = Color(0xFF4ADE80)
val RoyalError = Color(0xFFEF4444)
val RoyalPurple = Color(0xFF8B5CF6)
val RoyalPurpleDark = Color(0xFF4C1D95)

// Text Colors
val TextGold = Color(0xFFFFDF73)
val TextPrimary = Color(0xFFF3F3F7)
val TextSecondary = Color(0xFFA1A1B2)
val TextMuted = Color(0xFF6E6E80)

// Brushes
val RoyalGoldGradient = Brush.linearGradient(
    colors = listOf(
        RoyalGoldLight,
        RoyalGold,
        RoyalGoldDark
    )
)

val RoyalGoldHorizontalGradient = Brush.horizontalGradient(
    colors = listOf(
        RoyalGoldDark,
        RoyalGoldLight,
        RoyalGold,
        RoyalGoldLight,
        RoyalGoldDark
    )
)

val RoyalCrownGradient = Brush.verticalGradient(
    colors = listOf(
        RoyalGoldBright,
        RoyalGold,
        RoyalGoldDeep
    )
)

val DarkGlassGradient = Brush.verticalGradient(
    colors = listOf(
        MidnightSurfaceElevated.copy(alpha = 0.92f),
        MidnightSurface.copy(alpha = 0.95f)
    )
)

val PurpleGoldGlow = Brush.linearGradient(
    colors = listOf(
        RoyalPurple.copy(alpha = 0.25f),
        RoyalGold.copy(alpha = 0.15f),
        Color.Transparent
    )
)
