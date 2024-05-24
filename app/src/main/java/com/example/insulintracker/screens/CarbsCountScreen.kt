package com.example.insulintracker.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CarbsCountScreen() {
    var showDialog by remember { mutableStateOf(false) }
    val mealList = remember { mutableStateListOf<MealEntry>() }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            LazyColumn {
                items(mealList) { meal ->
                    Text(text = "${meal.mealTime} - УК: ${meal.uk}", fontSize = 20.sp)
                }
            }
        }
    }

    if (showDialog) {
        MealInputDialog(onDismiss = { showDialog = false }, onSave = { meal ->
            mealList.add(meal)
            showDialog = false
        })
    }
}

data class MealEntry(
    val mealTime: String,
    val uk: Double
)

@Composable
fun MealInputDialog(onDismiss: () -> Unit, onSave: (MealEntry) -> Unit) {
    var xe by remember { mutableStateOf("10") }
    var mealTime by remember { mutableStateOf("Завтрак") }
    var stSk by remember { mutableStateOf("") }
    var otrabotkaSk by remember { mutableStateOf("") }
    var fchi by remember { mutableStateOf("") }

    var dozList = remember { mutableStateListOf("") }
    var carbsList = remember { mutableStateListOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Введите данные") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                DropdownMenuField(label = "1 ХЕ", selectedItem = xe, items = listOf("10", "12")) { xe = it }
                Spacer(modifier = Modifier.height(8.dp))
                DropdownMenuField(label = "Наименование приема пищи", selectedItem = mealTime, items = listOf("Завтрак", "Обед", "Ужин")) { mealTime = it }
                Spacer(modifier = Modifier.height(8.dp))
                InputField("Ст ск", stSk) { stSk = it }
                Spacer(modifier = Modifier.height(8.dp))
                InputField("Отработка ск", otrabotkaSk) { otrabotkaSk = it }
                Spacer(modifier = Modifier.height(8.dp))
                InputField("ФЧИ", fchi) { fchi = it }
                Spacer(modifier = Modifier.height(8.dp))
                EditableList(label = "Дозировка", items = dozList) { dozList = it }
                Spacer(modifier = Modifier.height(8.dp))
                EditableList(label = "Кол-во углеводов", items = carbsList) { carbsList = it }
            }
        },
        confirmButton = {
            Button(onClick = {
                val stSkValue = stSk.toDoubleOrNull() ?: 0.0
                val otrabotkaSkValue = otrabotkaSk.toDoubleOrNull() ?: 0.0
                val fchiValue = fchi.toDoubleOrNull() ?: 0.0
                val dozValue = dozList.sumOf { it.toDoubleOrNull() ?: 0.0 }
                val carbsValue = carbsList.sumOf { it.toDoubleOrNull() ?: 0.0 }

                val uk = calculateUk(stSkValue, otrabotkaSkValue, fchiValue, dozValue, carbsValue, xe.toInt())
                onSave(MealEntry(mealTime, uk))
            }) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}

@Composable
fun InputField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = { if (it.all { char -> char.isDigit() || char == '.' }) onValueChange(it) },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun DropdownMenuField(label: String, selectedItem: String, items: List<String>, onItemSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(text = label)
        Box {
            Text(
                text = selectedItem,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { expanded = true }
            )

            androidx.compose.material3.DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = {
                            onItemSelected(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun EditableList(label: String, items: SnapshotStateList<String>, onItemsChange: (SnapshotStateList<String>) -> Unit) {
    Column {
        items.forEachIndexed { index, value ->
            InputField("$label ${index + 1}", value) { items[index] = it }
        }
        Button(onClick = { items.add("") }) {
            Text("+")
        }
    }
}

fun calculateUk(stSk: Double, otrabotkaSk: Double, fchi: Double, doz: Double, carbs: Double, xe: Int): Double {
    return ((stSk - otrabotkaSk) / fchi + doz) / (carbs / xe)
}
