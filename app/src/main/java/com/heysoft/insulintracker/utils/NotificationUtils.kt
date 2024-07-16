package com.heysoft.insulintracker.utils

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.heysoft.insulintracker.data.db.Event
import com.heysoft.insulintracker.data.db.EventDao
import com.heysoft.insulintracker.workers.NotificationWorker
import java.util.UUID
import java.util.concurrent.TimeUnit

suspend fun scheduleNotification(context: Context, event: Event, eventId: Int, eventDao: EventDao) {
    val timeDiff = calculateTimeDiff(event.date, event.time)
    if (timeDiff > 0) {
        val data = Data.Builder().putString("eventTitle", "Напоминание")
            .putString("eventDescription", event.note).putInt("eventId", eventId).build()

        val notificationWork = OneTimeWorkRequestBuilder<NotificationWorker>().setInitialDelay(
                timeDiff,
                TimeUnit.MILLISECONDS
            ).setInputData(data).build()

        WorkManager.getInstance(context).enqueue(notificationWork)

        eventDao.updateEventWorkId(eventId, notificationWork.id.toString())
    }
}

suspend fun cancelScheduledNotification(context: Context, eventId: Int, eventDao: EventDao) {
    val workId = eventDao.getEventWorkId(eventId)
    if (workId != null) {
        WorkManager.getInstance(context).cancelWorkById(UUID.fromString(workId))
        eventDao.updateEventWorkId(eventId, null)
    }
}
