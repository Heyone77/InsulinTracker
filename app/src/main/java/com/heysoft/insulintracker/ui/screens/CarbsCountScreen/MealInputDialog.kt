package com.heysoft.insulintracker.ui.screens.CarbsCountScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.heysoft.insulintracker.data.db.MealEntry
import com.heysoft.insulintracker.utils.calculateUk
import com.heysoft.insulintracker.utils.dateToUnix
import com.heysoft.insulintracker.utils.getCurrentDate
import com.heysoft.insulintracker.utils.intToMealTime
import com.heysoft.insulintracker.utils.mealTimeToInt
import com.heysoft.insulintracker.utils.toDoubleOrNullWithCommaSupport
import com.heysoft.insulintracker.utils.unixToDate
import com.heysoft.insulintracker.viewmodel.SharedViewModel

@Composable
fun MealInputDialog(
    onDismiss: () -> Unit,
    onSave: (MealEntry) -> Unit,
    onUpdate: ((MealEntry) -> Unit)? = null,
    mealEntryToEdit: MealEntry? = null,
    sharedViewModel: SharedViewModel
) {
    var xe by remember { mutableStateOf("10") }
    var mealTime by remember { mutableStateOf(mealEntryToEdit?.mealTimeInt?.let { intToMealTime(it) } ?: "Завтрак") }
    var stSk by remember { mutableStateOf(mealEntryToEdit?.stSk?.toString() ?: "") }
    var otrabotkaSk by remember { mutableStateOf(mealEntryToEdit?.otrabotkaSk?.toString() ?: "") }
    var fchi by remember { mutableStateOf(mealEntryToEdit?.fchi?.toString() ?: "") }
    var selectedDate by remember { mutableStateOf(mealEntryToEdit?.dateUnix?.let { unixToDate(it) } ?: getCurrentDate()) }

    val dozList = remember { mutableStateListOf("") }
    val carbsList = remember { mutableStateListOf("") }
    val fchiState by sharedViewModel.fchiValue.collectAsState()

    val isSaveEnabled by remember {
        derivedStateOf {
            stSk.isNotBlank() && otrabotkaSk.isNotBlank() && fchi.isNotBlank() &&
                    dozList.all { it.isNotBlank() } && carbsList.all { it.isNotBlank() } &&
                    stSk.toDoubleOrNullWithCommaSupport() != null && otrabotkaSk.toDoubleOrNullWithCommaSupport() != null &&
                    fchi.toDoubleOrNullWithCommaSupport() != null && dozList.all { it.toDoubleOrNullWithCommaSupport() != null } &&
                    carbsList.all { it.toDoubleOrNullWithCommaSupport() != null }
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
                        fchiState.let {
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
                        val stSkValue = stSk.toDoubleOrNullWithCommaSupport() ?: 0.0
                        val otrabotkaSkValue = otrabotkaSk.toDoubleOrNullWithCommaSupport() ?: 0.0
                        val fchiValue = fchi.toDoubleOrNullWithCommaSupport() ?: 0.0
                        val dozValue = dozList.sumOf { it.toDoubleOrNullWithCommaSupport() ?: 0.0 }
                        val carbsValue = carbsList.sumOf { it.toDoubleOrNullWithCommaSupport() ?: 0.0 }

                        val uk = calculateUk(
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
                            stSk = stSkValue,
                            otrabotkaSk = otrabotkaSkValue,
                            fchi = fchiValue,
                            doz = dozValue,
                            carbs = carbsValue,
                            uk = uk
                        ) ?: MealEntry(
                            dateUnix = dateToUnix(selectedDate),
                            mealTimeInt = mealTimeToInt(mealTime),
                            stSk = stSkValue,
                            otrabotkaSk = otrabotkaSkValue,
                            fchi = fchiValue,
                            doz = dozValue,
                            carbs = carbsValue,
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
