package com.heysoft.insulintracker.ui.theme

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object ThemePreferences {
    private const val PREFS_NAME = "theme_prefs"
    const val KEY_IS_DARK_THEME = "is_dark_theme"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun setDarkTheme(context: Context, isDarkTheme: Boolean) {
        Log.d("ThemePreferences", "setDarkTheme: Setting theme to ${if (isDarkTheme) "dark" else "light"}")
        getPreferences(context).edit().putBoolean(KEY_IS_DARK_THEME, isDarkTheme).apply()
    }

    fun isDarkTheme(context: Context): Boolean {
        val isDark = getPreferences(context).getBoolean(KEY_IS_DARK_THEME, false)
        Log.d("ThemePreferences", "isDarkTheme: Theme is ${if (isDark) "dark" else "light"}")
        return isDark
    }
}