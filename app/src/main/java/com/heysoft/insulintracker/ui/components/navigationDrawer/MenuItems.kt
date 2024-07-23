package com.heysoft.insulintracker.ui.components.navigationDrawer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

val menuItems = listOf(
    MenuItem("Расчёт УК", Icons.Outlined.Circle, "carbsCountScreen"),
    MenuItem("Перерасчет УК", Icons.Outlined.Circle, "recountCarbsCountScreen"),
    MenuItem("Расчёт ФЧИ", Icons.Outlined.Circle, "threeDaysInsulin"),
    MenuItem("Добавить событие", Icons.Default.Event, "addEventScreen"),
    MenuItem("Чаты и каналы с полезной информацией", Icons.AutoMirrored.Filled.Chat, "chatScreen"),
    MenuItem("Настройки", Icons.Default.Settings, "settingsScreen"),
    MenuItem("О приложении", Icons.AutoMirrored.Filled.Help, "aboutScreen")
)