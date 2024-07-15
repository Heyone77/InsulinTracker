package com.heysoft.insulintracker.workers

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.heysoft.insulintracker.viewmodel.SharedViewModel

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "DELETE_EVENT") {
            val eventId = intent.getIntExtra("eventId", -1)
            if (eventId != -1) {
                val sharedViewModel: SharedViewModel = ViewModelProvider(
                    ViewModelStore(),
                    ViewModelProvider.AndroidViewModelFactory.getInstance(context.applicationContext as Application)
                )[SharedViewModel::class.java]

                sharedViewModel.deleteEvent(eventId)

                Log.d("NotificationReceiver", "Event with ID: $eventId deleted.")
            }
        }
    }
}