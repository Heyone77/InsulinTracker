package com.heysoft.insulintracker.ui.screens.EventListScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.heysoft.insulintracker.data.db.Event
import com.heysoft.insulintracker.viewmodel.SharedViewModel

@Composable
fun EventListScreen(modifier: Modifier = Modifier) {
    val sharedViewModel: SharedViewModel = hiltViewModel()
    LocalContext.current
    val events by sharedViewModel.allEvents.collectAsState(initial = emptyList())
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        EventDialog(
            onDismiss = { showDialog = false },
            onSave = { event ->
                sharedViewModel.insertEvent(event)
                showDialog = false
            }
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Event")
            }
        },
        content = { padding ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                LazyColumn {
                    items(events) { event ->
                        EventCard(event = event, onDelete = { sharedViewModel.deleteEvent(it) })
                    }
                }
            }
        }
    )
}

@Composable
fun EventCard(event: Event, onDelete: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = event.date, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
                Text(text = event.time, color = MaterialTheme.colorScheme.onPrimary)
                Text(text = event.note, color = MaterialTheme.colorScheme.onPrimary)
            }

            IconButton(onClick = { onDelete(event.id) }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}
