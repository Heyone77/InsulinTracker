package com.heysoft.insulintracker

import RecountCarbsCountScreen
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Help
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.heysoft.insulintracker.screens.AboutScreen
import com.heysoft.insulintracker.screens.CarbsCountScreen
import com.heysoft.insulintracker.screens.ScreenInfoDialog
import com.heysoft.insulintracker.screens.SettingsScreen
import com.heysoft.insulintracker.screens.ThreeDaysInsulinScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(sharedViewModel: SharedViewModel, isDarkTheme: MutableState<Boolean>) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var currentScreen by remember { mutableStateOf("Учет углеводов") }
    var showInfoDialog by remember { mutableStateOf(false) }

    val screenTitleMap = mapOf(
        "carbsCountScreen" to "Расчёт УК",
        "recountCarbsCountScreen" to "Перерасчет УК",
        "threeDaysInsulin" to "ФЧИ",
        "aboutScreen" to "О приложении",
        "settingsScreen" to "Настройки",
        "userAgreementScreen" to "Пользовательское соглашение"
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(240.dp), drawerShape = RectangleShape) {
                DrawerContent(navController, drawerState, scope) { screen ->
                    currentScreen = screenTitleMap[screen] ?: "Unknown"
                }
            }
        },
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(currentScreen) },
                        navigationIcon = {
                            if (currentScreen != "Пользовательское соглашение") {
                                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                    Icon(Icons.Filled.Menu, contentDescription = "Menu")
                                }
                            }
                        },
                        actions = {
                            if (currentScreen != "Пользовательское соглашение") {
                                IconButton(onClick = { showInfoDialog = true }) {
                                    Icon(Icons.AutoMirrored.Outlined.Help, contentDescription = "Info")
                                }
                            }
                        }
                    )
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = if (sharedViewModel.isAgreementAccepted.value == true) "carbsCountScreen" else "userAgreementScreen",
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable("carbsCountScreen") {
                        currentScreen = screenTitleMap["carbsCountScreen"] ?: "Расчёт УК"
                        CarbsCountScreen(sharedViewModel = sharedViewModel)
                    }
                    composable("recountCarbsCountScreen") {
                        currentScreen = screenTitleMap["recountCarbsCountScreen"] ?: "Перерасчет УК"
                        RecountCarbsCountScreen()
                    }
                    composable("threeDaysInsulin") {
                        currentScreen = screenTitleMap["threeDaysInsulin"] ?: "ФЧИ"
                        ThreeDaysInsulinScreen(sharedViewModel = sharedViewModel)
                    }
                    composable("aboutScreen") {
                        currentScreen = screenTitleMap["aboutScreen"] ?: "О приложении"
                        AboutScreen()
                    }
                    composable("settingsScreen") {
                        currentScreen = screenTitleMap["settingsScreen"] ?: "Настройки"
                        SettingsScreen(isDarkTheme = isDarkTheme)
                    }
                    composable("userAgreementScreen") {
                        currentScreen = screenTitleMap["userAgreementScreen"] ?: "Пользовательское соглашение"
                        UserAgreementScreen(sharedViewModel, navController) {
                            navController.navigate("carbsCountScreen") {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    }
                }

                if (showInfoDialog) {
                    ScreenInfoDialog(
                        screenName = currentScreen,
                        onDismiss = { showInfoDialog = false }
                    )
                }
            }
        }
    )
}

