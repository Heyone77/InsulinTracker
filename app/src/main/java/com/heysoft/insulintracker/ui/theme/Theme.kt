package com.heysoft.insulintracker.ui.theme

import MyTypography
import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.compose.material3.MaterialTheme
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

private val DarkColorScheme = darkColorScheme(
    primary = DarkGreen,
    secondary = DarkGreenGrey,
    tertiary = GreenPink,
    background = Color.DarkGray,
    surface = Color.DarkGray,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.DarkGray,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = LightGreen,
    secondary = LightGreenGrey,
    tertiary = GreenPink,
    background = Color.White,
    surface = Color.LightGray,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun InsulinTrackerTheme(
    isDarkTheme: Boolean,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    Log.d("InsulinTrackerTheme", "Applying theme: ${if (isDarkTheme) "Dark" else "Light"}")

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (isDarkTheme) {
                Log.d("InsulinTrackerTheme", "Using dynamic dark color scheme")
                dynamicDarkColorScheme(context)
            } else {
                Log.d("InsulinTrackerTheme", "Using dynamic light color scheme")
                dynamicLightColorScheme(context)
            }
        }
        isDarkTheme -> {
            Log.d("InsulinTrackerTheme", "Using static dark color scheme")
            DarkColorScheme
        }
        else -> {
            Log.d("InsulinTrackerTheme", "Using static light color scheme")
            LightColorScheme
        }
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDarkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MyTypography,
        content = content
    )
}