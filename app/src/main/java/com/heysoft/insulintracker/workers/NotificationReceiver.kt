package com.heysoft.insulintracker.workers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.heysoft.insulintracker.data.repository.EventRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationReceiver : BroadcastReceiver() {

    @Inject
    lateinit var eventRepository: EventRepository

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "DELETE_EVENT") {
            val eventId = intent.getIntExtra("eventId", -1)
            if (eventId != -1) {
                CoroutineScope(Dispatchers.IO).launch {
                    eventRepository.deleteEvent(eventId)
                    Log.d("NotificationReceiver", "Event with ID $eventId deleted")
                    NotificationManagerCompat.from(context).cancel(eventId)
                }
            }
        }
    }
}
