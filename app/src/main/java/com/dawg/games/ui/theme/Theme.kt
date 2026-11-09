package com.dawg.games.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = GamePrimary,
    secondary = GameSecondary,
    tertiary = GameAccentGold,
    background = GameBackground,
    surface = GameSurface,
    surfaceVariant = GameSurfaceVariant,
    onPrimary = GameTextPrimary,
    onSecondary = GameBackground,
    onBackground = GameTextPrimary,
    onSurface = GameTextPrimary,
    onSurfaceVariant = GameTextSecondary
)

@Composable
fun DAWGTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
