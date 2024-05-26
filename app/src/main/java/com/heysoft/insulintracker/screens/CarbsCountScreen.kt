package com.heysoft.insulintracker.screens

import android.widget.DatePicker
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.DialogProperties
import com.heysoft.insulintracker.MealEntry
import com.heysoft.insulintracker.SharedViewModel
import com.heysoft.insulintracker.calculateUk
import com.heysoft.insulintracker.dateToUnix
import com.heysoft.insulintracker.getCurrentDate
import com.heysoft.insulintracker.intToMealTime
import com.heysoft.insulintracker.mealTimeToInt
import com.heysoft.insulintracker.unixToDate
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun CarbsCountScreen(sharedViewModel: SharedViewModel) {
    LaunchedEffect(Unit) {
        sharedViewModel.loadMealEntries()
    }

    val mealEntries by sharedViewModel.mealEntries.observeAsState(emptyList())
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
                Text("Нет данных для отображения", fontSize = 20.sp, modifier = Modifier.padding(16.dp))
            } else {
                LazyColumn {
                    items(mealEntries) { meal ->
                        val ukText =
                            if (meal.uk % 1.0 == 0.0) meal.uk.toInt().toString() else meal.uk.toString()
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedMealEntry = meal
                                    showEditDeleteDialog = true
                                }
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "${unixToDate(meal.dateUnix)} - ${intToMealTime(meal.mealTimeInt)} - УК: $ukText", fontSize = 20.sp)
                        }
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
                mealEntry = mealEntry,
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

@Composable
fun MealInputDialog(
    onDismiss: () -> Unit,
    onSave: (MealEntry) -> Unit,
    onUpdate: ((MealEntry) -> Unit)? = null,
    mealEntryToEdit: MealEntry? = null,
    sharedViewModel: SharedViewModel
) {
    var xe by remember { mutableStateOf("10") }  // Set default value for XE
    var mealTime by remember { mutableStateOf(mealEntryToEdit?.mealTimeInt?.let { intToMealTime(it) } ?: "Завтрак") }
    var stSk by remember { mutableStateOf("") }
    var otrabotkaSk by remember { mutableStateOf("") }
    var fchi by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(mealEntryToEdit?.dateUnix?.let { unixToDate(it) } ?: getCurrentDate()) }

    // Only use mealEntryToEdit?.uk for display purposes, not to initialize XE
    val initialUk = mealEntryToEdit?.uk?.toString() ?: ""

    val dozList = remember { mutableStateListOf("") }
    val carbsList = remember { mutableStateListOf("") }

    val fchiState by sharedViewModel.fchiValue.observeAsState()

    val isSaveEnabled by remember {
        derivedStateOf {
            stSk.isNotBlank() && otrabotkaSk.isNotBlank() && fchi.isNotBlank() &&
                    dozList.all { it.isNotBlank() } && carbsList.all { it.isNotBlank() } &&
                    stSk.toDoubleOrNull() != null && otrabotkaSk.toDoubleOrNull() != null &&
                    fchi.toDoubleOrNull() != null && dozList.all { it.toDoubleOrNull() != null } &&
                    carbsList.all { it.toDoubleOrNull() != null }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        title = { Text(if (mealEntryToEdit == null) "Введите данные" else "Редактировать данные") },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DatePickerField(label = "Выберите дату", selectedDate = selectedDate) { selectedDate = it }
                DropdownMenuField(
                    label = "1 хлебная единица (ХЕ) = ГУ",
                    selectedItem = xe,
                    items = listOf("10", "12")
                ) { xe = it }
                DropdownMenuField(
                    label = "Наименование приема пищи",
                    selectedItem = mealTime,
                    items = listOf("Завтрак", "Второй завтрак", "Обед", "Полдник", "Ужин", "Поздний ужин")
                ) { mealTime = it }
                InputField("Старт СК", stSk) { stSk = it }
                InputField("Отработка СК", otrabotkaSk) { otrabotkaSk = it }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    InputField("ФЧИ", fchi, Modifier.weight(1f)) { fchi = it }
                    Button(onClick = {
                        fchiState?.let {
                            fchi = it.toString()
                        }
                    }, shape = RectangleShape) {
                        Text("Импорт ФЧИ")
                    }
                }
                EditableList(label = "Кол-во инс. на еду (ед.)", items = dozList)
                EditableList(label = "Кол-во углеводов", items = carbsList)
            }
        },
        confirmButton = {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = onDismiss, shape = RectangleShape) {
                    Text("Отмена")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = {
                    if (isSaveEnabled) {
                        val stSkValue: Double = stSk.toDoubleOrNull() ?: 0.0
                        val otrabotkaSkValue: Double = otrabotkaSk.toDoubleOrNull() ?: 0.0
                        val fchiValue: Double = fchi.toDoubleOrNull() ?: 0.0
                        val dozValue: Double = dozList.sumOf { it.toDoubleOrNull() ?: 0.0 }
                        val carbsValue: Double = carbsList.sumOf { it.toDoubleOrNull() ?: 0.0 }

                        val uk: Double = calculateUk(
                            stSkValue,
                            otrabotkaSkValue,
                            fchiValue,
                            dozValue,
                            carbsValue,
                            xe.toInt()
                        )

                        val updatedMealEntry = mealEntryToEdit?.copy(
                            dateUnix = dateToUnix(selectedDate),
                            mealTimeInt = mealTimeToInt(mealTime),
                            uk = uk
                        ) ?: MealEntry(
                            dateUnix = dateToUnix(selectedDate),
                            mealTimeInt = mealTimeToInt(mealTime),
                            uk = uk
                        )

                        if (mealEntryToEdit == null) {
                            onSave(updatedMealEntry)
                        } else {
                            onUpdate?.invoke(updatedMealEntry)
                        }
                    }
                }, enabled = isSaveEnabled, shape = RectangleShape) {
                    Text("Сохранить")
                }
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(label: String, selectedDate: String, onDateSelected: (String) -> Unit) {
    LocalContext.current
    val calendar = Calendar.getInstance()
    val dateFormatter = SimpleDateFormat("dd-MMM-yy", Locale.getDefault())

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Отмена")
                }
            },
            content = {
                AndroidView(
                    factory = { context ->
                        DatePicker(context).apply {
                            init(year, month, day) { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                                val selectedCalendar = Calendar.getInstance()
                                selectedCalendar.set(selectedYear, selectedMonth, selectedDayOfMonth)
                                onDateSelected(dateFormatter.format(selectedCalendar.time))
                            }
                        }
                    }
                )
            }
        )
    }

    Column {
        Text(text = label)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.primary)
                .clickable { showDialog = true }
                .padding(8.dp)
        ) {
            Text(
                text = selectedDate,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun EditDeleteMealDialog(
    mealEntry: MealEntry,
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Редактировать или удалить запись") },
        text = {
            Column {
                Text(text = "Выберите действие для записи:")
                Text(text = "${unixToDate(mealEntry.dateUnix)} - ${intToMealTime(mealEntry.mealTimeInt)} - УК: ${mealEntry.uk}")
            }
        },
        confirmButton = {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = onEdit) {
                    Text("Редактировать")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = onDelete) {
                    Text("Удалить")
                }
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
fun InputField(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = { if (it.all { char -> char.isDigit() || char == '.' }) onValueChange(it) },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun DropdownMenuField(
    label: String,
    selectedItem: String,
    items: List<String>,
    onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(text = label)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.primary)
                .clickable { expanded = true }
                .padding(8.dp)
        ) {
            Text(
                text = selectedItem,
                modifier = Modifier.fillMaxWidth()
            )

            DropdownMenu(
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
fun EditableList(label: String, items: SnapshotStateList<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items.forEachIndexed { index, value ->
            InputField("$label ${index + 1}", value) { items[index] = it }
        }
        Button(
            onClick = { items.add("") },
            modifier = Modifier.fillMaxWidth(),
            shape = RectangleShape
        ) {
            Text("+")
        }
    }
}

