package com.heysoft.insulintracker


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            label = "О приложении",
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
fun NavigationDrawerItem(label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 18.sp,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
