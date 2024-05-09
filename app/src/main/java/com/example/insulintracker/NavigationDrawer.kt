package com.example.insulintracker

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomModalNavigationDrawer() {
    val (selectedScreen, setSelectedScreen) = remember { mutableStateOf("Расчет УК") }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(selectedScreen, setSelectedScreen, scope, drawerState)
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("App Title") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                if (drawerState.isClosed) {
                                    drawerState.open()
                                } else {
                                    drawerState.close()
                                }
                            }
                        }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        IconButton(onClick = { setShowDialog(true) }) {
                            Icon(Icons.AutoMirrored.Filled.Help, contentDescription = "Help")
                        }
                    }
                )
            }
        ) { paddingValues ->
            MainContent(selectedScreen, Modifier.padding(paddingValues))
        }
    }

    if (showDialog) {
        InfoDialog(selectedScreen, onDismiss = { setShowDialog(false) })
    }
}

@Composable
fun InfoDialog(selectedScreen: String, onDismiss: () -> Unit) {
    val title = "Информация о $selectedScreen"
    val infoText = when (selectedScreen) {
        "Расчет УК" -> "Information about the Home screen."
        "Перерасчет УК" -> "Details on Item 1."
        "Расчет ФЧИ" -> "Details on Item 2."
        "О приложении" -> "Your activity history."
        else -> "No specific information available."
    }
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = title) },
        text = { Text(text = infoText) },
        confirmButton = {
            Button(onClick = { onDismiss() }) {
                Text("OK")
            }
        }
    )
}

@Composable
fun DrawerContent(
    selectedScreen: String,
    setSelectedScreen: (String) -> Unit,
    scope: CoroutineScope,
    drawerState: DrawerState,

) {
    ModalDrawerSheet(modifier =  Modifier.width(240.dp), drawerShape = RectangleShape) {
        val items = listOf("Перерасчет УК", "Расчет ФЧИ", "О приложении")
        Spacer(modifier = Modifier.height(10.dp))
        NavigationDrawerItem(
            label = { Text(text = "Расчет УК") },
            selected = selectedScreen == "Расчет УК",
            onClick = {
                setSelectedScreen("Расчет УК")
                scope.launch {
                    drawerState.close()
                }
            },
            shape = RectangleShape

        )
        items.forEach { item ->
            NavigationDrawerItem(
                label = { Text(text = item) },
                selected = selectedScreen == item,
                onClick = {
                    setSelectedScreen(item)
                    scope.launch {
                        drawerState.close()
                    }
                },
                shape = RectangleShape
            )
        }
    }
}

@Composable
fun MainContent(selectedScreen: String, modifier: Modifier = Modifier) {
    when (selectedScreen) {
        "Расчет УК" -> Text("Расчет УК", modifier = modifier)
        "Перерасчет УК" -> Text("Content for Item 1", modifier = modifier)
        "Расчет ФЧИ" -> Text("Content for Item 2", modifier = modifier)
        "О приложении" -> Text("History Content", modifier = modifier)
        else -> Text("Home Screen", modifier = modifier)
    }
}