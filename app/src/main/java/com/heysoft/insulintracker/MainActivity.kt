package com.heysoft.insulintracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.heysoft.insulintracker.ui.theme.InsulinTrackerTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val sharedViewModel: SharedViewModel = viewModel()
            val isDarkTheme = remember { mutableStateOf(ThemePreferences.isDarkTheme(this)) }

            InsulinTrackerTheme(
                darkTheme = isDarkTheme.value,
                onThemeChange = { newTheme ->
                    isDarkTheme.value = newTheme
                    ThemePreferences.setDarkTheme(this, newTheme)
                }
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(sharedViewModel = sharedViewModel, isDarkTheme = isDarkTheme)
                }
            }
        }
    }
}