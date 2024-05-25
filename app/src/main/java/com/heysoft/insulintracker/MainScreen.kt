package com.heysoft.insulintracker

import AboutScreen
import RecountCarbsCountScreen
import ScreenInfoDialog
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.heysoft.insulintracker.screens.CarbsCountScreen
import com.heysoft.insulintracker.screens.ThreeDaysInsulinScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(sharedViewModel: SharedViewModel) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentScreen = remember { mutableStateOf("Учет углеводов") }
    var showInfoDialog by remember { mutableStateOf(false) }

    val screenTitleMap = mapOf(
        "carbsCountScreen" to "Расчёт УК",
        "recountCarbsCountScreen" to "Перерасчет УК",
        "threeDaysInsulin" to "ФЧИ",
        "aboutScreen" to "О приложении"
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(240.dp)) {
                DrawerContent(navController, drawerState, scope) { screen ->
                    currentScreen.value = screenTitleMap[screen] ?: "Unknown"
                }
            }
        },
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(currentScreen.value) },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Filled.Menu, contentDescription = "Menu")
                            }
                        },
                        actions = {
                            IconButton(onClick = { showInfoDialog = true }) {
                                Icon(Icons.Filled.Info, contentDescription = "Info")
                            }
                        }
                    )
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = "recountCarbsCountScreen",
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable("carbsCountScreen") {
                        currentScreen.value = screenTitleMap["carbsCountScreen"] ?: "Расчёт УК"
                        CarbsCountScreen(sharedViewModel = sharedViewModel)
                    }
                    composable("recountCarbsCountScreen") {
                        currentScreen.value =
                            screenTitleMap["recountCarbsCountScreen"] ?: "Перерасчет УК"
                        RecountCarbsCountScreen()
                    }
                    composable("threeDaysInsulin") {
                        currentScreen.value =
                            screenTitleMap["threeDaysInsulin"] ?: "ФЧИ"
                        ThreeDaysInsulinScreen(sharedViewModel = sharedViewModel)
                    }
                    composable("aboutScreen") {
                        currentScreen.value = screenTitleMap["aboutScreen"] ?: "О приложении"
                        AboutScreen()
                    }
                }

                if (showInfoDialog) {
                    ScreenInfoDialog(
                        screenName = currentScreen.value,
                        onDismiss = { showInfoDialog = false }
                    )
                }
            }
        }

    )
}


