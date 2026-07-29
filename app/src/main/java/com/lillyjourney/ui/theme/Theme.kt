package com.lillyjourney.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = BackgroundCard,
    onPrimaryContainer = PrimaryDark,
    secondary = Secondary,
    onSecondary = OnPrimary,
    secondaryContainer = BackgroundCard,
    onSecondaryContainer = TextSecondary,
    tertiary = Success,
    onTertiary = OnPrimary,
    background = Background,
    onBackground = TextPrimary,
    surface = SurfaceLight,
    onSurface = OnSurface,
    surfaceVariant = SurfaceContainer,
    onSurfaceVariant = OnSurfaceVariant,
    outline = Outline,
    outlineVariant = Border,
    error = Error,
    onError = OnError,
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDarkTheme,
    onPrimary = BackgroundDark,
    primaryContainer = PrimaryDark,
    onPrimaryContainer = Background,
    secondary = SecondaryDarkTheme,
    onSecondary = BackgroundDark,
    background = BackgroundDark,
    onBackground = OnSurfaceDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = OnSurfaceVariantDark.copy(alpha = 0.3f),
    error = Danger,
    onError = BackgroundDark,
)

@Composable
fun LillyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = LillyTypography,
        content = content
    )
}
