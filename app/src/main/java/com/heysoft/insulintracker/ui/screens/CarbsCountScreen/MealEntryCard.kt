package com.heysoft.insulintracker.ui.screens.CarbsCountScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.heysoft.insulintracker.data.db.MealEntry
import com.heysoft.insulintracker.utils.intToMealTime
import com.heysoft.insulintracker.utils.unixToDate
import java.util.Locale

@Composable
fun MealEntryCard(meal: MealEntry, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Дата: ${unixToDate(meal.dateUnix)}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Прием пищи: ${intToMealTime(meal.mealTimeInt)}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "УК: ${String.format(Locale.US, "%.2f", meal.uk)}", style = MaterialTheme.typography.bodyLarge)
        }
    }
}