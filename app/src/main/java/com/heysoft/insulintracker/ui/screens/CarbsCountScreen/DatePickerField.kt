package com.heysoft.insulintracker.ui.screens.CarbsCountScreen

import android.widget.DatePicker
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(label: String, selectedDate: String, onDateSelected: (String) -> Unit) {
    LocalContext.current
    val calendar = Calendar.getInstance()
    val dateFormatter = SimpleDateFormat("dd MMM yy", Locale.getDefault())

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
