package com.example.todolist.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Your existing DarkColorScheme - Consider adjusting background and surface colors
// if they are the same as your primary color, as this might reduce contrast
// for elements placed on them.
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF6A4C93), // Deep Purple - Good
    onPrimary = Color.White,    // Text/icons on primary
    primaryContainer = Color(0xFF4C366B), // A slightly darker or lighter shade of primary
    onPrimaryContainer = Color(0xFFEADDFF), // Text/icons on primaryContainer

    secondary = Color(0xFF9C7BB3), // Lighter Purple - Good
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF5F487A),
    onSecondaryContainer = Color(0xFFE8DDFF),

    tertiary = Color(0xFFB59BD6), // Even Lighter Purple - Good
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF765F93),
    onTertiaryContainer = Color(0xFFF2DAFF),

    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
    errorContainer = Color(0xFF8C1D18),
    onErrorContainer = Color(0xFFF9DEDC),

    background = Color(0xFF1D1A22), // Dark background, distinct from primary
    onBackground = Color(0xFFE7E0EC),   // Light text on dark background

    surface = Color(0xFF1D1A22), // Dark surface, can be same as background or slightly different
    onSurface = Color(0xFFE7E0EC),    // Light text on dark surface

    surfaceVariant = Color(0xFF49454F), // For card backgrounds, etc.
    onSurfaceVariant = Color(0xFFCAC4D0),

    outline = Color(0xFF938F99)
)

// Your existing LightColorScheme - Similar considerations for background/surface
// and ensure good contrast for onPrimary, onSecondary, etc.
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6A4C93), // Deep Purple
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEADDFF), // Light tint of primary
    onPrimaryContainer = Color(0xFF21005D), // Dark text on light primary container

    secondary = Color(0xFF9C7BB3), // Lighter Purple
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE8DDFF),
    onSecondaryContainer = Color(0xFF1D192B),

    tertiary = Color(0xFFB59BD6), // Even Lighter Purple
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFF2DAFF),
    onTertiaryContainer = Color(0xFF251431),

    error = Color(0xFFB3261E),
    onError = Color.White,
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = Color(0xFF410E0B),

    background = Color(0xFFFFFBFE), // Light background
    onBackground = Color(0xFF1C1B1F),   // Dark text on light background

    surface = Color(0xFFFFFBFE), // Light surface
    onSurface = Color(0xFF1C1B1F),    // Dark text on light surface

    surfaceVariant = Color(0xFFE7E0EC),
    onSurfaceVariant = Color(0xFF49454F),

    outline = Color(0xFF79747E)
)

@Composable
fun ToDoListTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    // Set to false if you want to use your custom defined color scheme strictly.
    dynamicColor: Boolean = false, // You can set this to true to try out Material You dynamic colors
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Handle Status Bar and Navigation Bar colors
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Set status bar color
            window.statusBarColor = colorScheme.background.toArgb() // Or use colorScheme.surface or a specific color
            // Set navigation bar color (optional)
            window.navigationBarColor = colorScheme.background.toArgb() // Or a specific color

            // Set status bar icons and navigation bar icons to be light or dark
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Make sure you have Typography defined in ui/theme/Type.kt
        content = content
    )
}