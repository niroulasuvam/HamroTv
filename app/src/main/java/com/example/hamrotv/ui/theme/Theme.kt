package com.example.hamrotv.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Define your custom colors based on your existing colors.xml
private val LightColors = lightColorScheme(
    primary = Color(0xFF2196F3),  // light_blue
    onPrimary = Color(0xFFFFFFFF),  // white
    secondary = Color(0xFF8E24AA),  // purple
    onSecondary = Color(0xFFFFFFFF),  // white
    background = Color(0xFFF5F5F5),  // light_gray
    onBackground = Color(0xFF000000),  // black
    surface = Color(0xFFFFFFFF),  // white
    onSurface = Color(0xFF000000),  // black
    surfaceVariant = Color(0xFFE3F2FD),  // edit_button_background
    onSurfaceVariant = Color(0xFF522C5D)  // light_purple
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF64B5F6),  // lighter blue for dark theme
    onPrimary = Color(0xFF000000),
    secondary = Color(0xFFBA68C8),  // lighter purple for dark theme
    onSecondary = Color(0xFF000000),
    background = Color(0xFF121212),
    onBackground = Color(0xFFFFFFFF),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFF2C2C2C),
    onSurfaceVariant = Color(0xFFBBBBBB)
)

@Composable
fun HamroTVTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}