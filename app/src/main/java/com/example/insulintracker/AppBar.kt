package com.example.insulintracker

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    onNavigationIconClick: () -> Unit
) {
    TopAppBar(
        title = { Text("Title") },
        navigationIcon = {
            IconButton(onClick = { onNavigationIconClick }) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
            }
        }
    )
}