package com.heysoft.insulintracker.ui.screens.CarbsCountScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.heysoft.insulintracker.data.db.MealEntry
import com.heysoft.insulintracker.viewmodel.SharedViewModel

@Composable
fun CarbsCountScreen() {
    val sharedViewModel: SharedViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        sharedViewModel.loadMealEntries()
    }

    val mealEntries by sharedViewModel.mealEntries.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var showEditDeleteDialog by remember { mutableStateOf(false) }
    var selectedMealEntry by remember { mutableStateOf<MealEntry?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    selectedMealEntry = null
                    showDialog = true
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Add",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            if (mealEntries.isEmpty()) {
                Text("Нет данных для отображения", fontSize = 20.sp, modifier = Modifier.padding(25.dp))
            } else {
                LazyColumn {
                    items(mealEntries) { meal ->
                        MealEntryCard(meal = meal, onClick = {
                            selectedMealEntry = meal
                            showEditDeleteDialog = true
                        })
                    }
                }
            }
        }
    }

    if (showDialog) {
        MealInputDialog(
            onDismiss = { showDialog = false },
            onSave = { meal ->
                sharedViewModel.insertMealEntry(meal)
                showDialog = false
            },
            onUpdate = { meal ->
                sharedViewModel.updateMealEntry(meal)
                showDialog = false
            },
            mealEntryToEdit = selectedMealEntry,
            sharedViewModel = sharedViewModel
        )
    }

    if (showEditDeleteDialog) {
        selectedMealEntry?.let { mealEntry ->
            EditDeleteMealDialog(
                onDismiss = { showEditDeleteDialog = false },
                onDelete = {
                    sharedViewModel.deleteMealEntry(mealEntry)
                    showEditDeleteDialog = false
                },
                onEdit = {
                    showEditDeleteDialog = false
                    showDialog = true
                }
            )
        }
    }
}
