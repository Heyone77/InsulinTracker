package com.heysoft.insulintracker

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotificationWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        val eventTitle = inputData.getString("eventTitle") ?: "Событие"
        val eventDescription = inputData.getString("eventDescription") ?: "Напоминание о событии"
        val eventId = inputData.getInt("eventId", -1)

        Log.d("NotificationWorker", "doWork: Creating notification with title: $eventTitle and description: $eventDescription")

        showNotification(eventTitle, eventDescription, eventId)
        return Result.success()
    }

    private fun showNotification(title: String, description: String, eventId: Int) {
        val notificationId = System.currentTimeMillis().toInt()
        val channelId = "event_channel"

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )


        val deleteIntent = Intent(applicationContext, NotificationReceiver::class.java).apply {
            action = "DELETE_EVENT"
            putExtra("eventId", eventId)
        }
        val deletePendingIntent: PendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            0,
            deleteIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .addAction(R.drawable.ic_notification, "Удалить", deletePendingIntent)  // Добавляем кнопку действия

        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            with(NotificationManagerCompat.from(applicationContext)) {
                notify(notificationId, builder.build())
            }
        }
    }

    companion object {
        fun createNotificationChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "Event Channel"
                val descriptionText = "Channel for event notifications"
                val importance = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel("event_channel", name, importance).apply {
                    description = descriptionText
                }
                val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }
    }
}