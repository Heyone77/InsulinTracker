package com.example.insulintracker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.insulintracker.screens.AboutScreen
import com.example.insulintracker.screens.CarbsCountScreen
import com.example.insulintracker.screens.RecountCarbsCountScreen
import com.example.insulintracker.screens.ThreeDaysInsulin

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    ModalNavigationDrawer(
        drawerContent = {
            DrawerContent(navController)
        }
    ) {
        NavHost(navController = navController, startDestination = "CarbsCountScreen") {
            composable("CarbsCountScreen") { CarbsCountScreen() }
            composable("RecountCarbsCountScreen") { RecountCarbsCountScreen() }
            composable("ThreeDaysInsulinScreen") { ThreeDaysInsulin() }
            composable("AboutScreen") { AboutScreen() }
        }
    }
}

@Composable
fun DrawerContent(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Home",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { navController.navigate("CarbsCountScreen") }
        )
        Text(
            text = "Profile",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { navController.navigate("RecountCarbsCountScreen") }
        )
        Text(
            text = "Settings",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { navController.navigate("ThreeDaysInsulinScreen") }
        )
        Text(
            text = "Help",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { navController.navigate("AboutScreen") }
        )
    }
}
