package com.heysoft.insulintracker.ui.theme

import android.content.Context
import android.content.SharedPreferences

object ThemePreferences {
    private const val PREFS_NAME = "theme_prefs"
    private const val KEY_IS_DARK_THEME = "is_dark_theme"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun setDarkTheme(context: Context, isDarkTheme: Boolean) {
        getPreferences(context).edit().putBoolean(KEY_IS_DARK_THEME, isDarkTheme).apply()
    }

    fun isDarkTheme(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_IS_DARK_THEME, false)
    }
}