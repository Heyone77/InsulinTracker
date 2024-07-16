package com.heysoft.insulintracker.ui.screens.CarbsCountScreen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EditDeleteMealDialog(
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Column {
                Text(text = "Выберите действие для записи")
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
