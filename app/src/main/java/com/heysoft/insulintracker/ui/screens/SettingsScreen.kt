package com.heysoft.insulintracker.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.heysoft.insulintracker.viewmodel.SharedViewModel

@Composable
fun SettingsScreen(sharedViewModel: SharedViewModel = hiltViewModel()) {
    val isDarkTheme by sharedViewModel.isDarkTheme.collectAsState()

    Log.d("SettingsScreen", "SettingsScreen: isDarkTheme = $isDarkTheme")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Темная тема", fontSize = 18.sp)
            Switch(
                checked = isDarkTheme,
                onCheckedChange = { newTheme ->
                    Log.d("SettingsScreen", "Switch clicked: newTheme = $newTheme")
                    sharedViewModel.setDarkTheme(newTheme)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    uncheckedThumbColor = Color.LightGray
                )
            )
        }
    }
}
