package com.heysoft.insulintracker.ui.screens.CarbsCountScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

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
