package com.heysoft.insulintracker


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun DrawerContent(
    navController: NavHostController,
    drawerState: DrawerState,
    scope: CoroutineScope,
    onScreenSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        NavigationDrawerItem(
            label = "Расчёт УК",
            icon = Icons.Outlined.Circle,
            onClick = {
                onScreenSelected("Расчёт УК")
                navController.navigate("carbsCountScreen") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
                scope.launch { drawerState.close() }
            }
        )
        NavigationDrawerItem(
            label = "Перерасчет УК",
            icon = Icons.Outlined.Circle,
            onClick = {
                onScreenSelected("Перерасчет УК")
                navController.navigate("recountCarbsCountScreen") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
                scope.launch { drawerState.close() }
            }
        )
        NavigationDrawerItem(
            label = "Расчёт ФЧИ",
            icon = Icons.Outlined.Circle,
            onClick = {
                onScreenSelected("ФЧИ")
                navController.navigate("threeDaysInsulin") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
                scope.launch { drawerState.close() }
            }
        )
        NavigationDrawerItem(
            label = "Добавить событие",
            icon = Icons.Default.Event,
            onClick = {
                onScreenSelected("Добавить событие")
                navController.navigate("addEventScreen") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
                scope.launch { drawerState.close() }
            }
        )
        NavigationDrawerItem(
            label = "Чаты и каналы с полезной информацией",
            icon = Icons.AutoMirrored.Filled.Chat,
            onClick = {
                onScreenSelected("Чаты и каналы с полезной информацией")
                navController.navigate("chatScreen") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
                scope.launch { drawerState.close() }
            }
        )
        NavigationDrawerItem(
            label = "Настройки",
            icon = Icons.Default.Settings,
            onClick = {
                onScreenSelected("Настройки")
                navController.navigate("settingsScreen") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
                scope.launch { drawerState.close() }
            }
        )
        NavigationDrawerItem(
            label = "О приложении",
            icon = Icons.AutoMirrored.Filled.Help,
            onClick = {
                onScreenSelected("О приложении")
                navController.navigate("aboutScreen") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
                scope.launch { drawerState.close() }
            }
        )
    }
}

@Composable
fun NavigationDrawerItem(label: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            fontSize = 18.sp,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
