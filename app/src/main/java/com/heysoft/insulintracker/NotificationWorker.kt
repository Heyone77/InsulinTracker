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

        Log.d("NotificationWorker", "doWork: Creating notification with title: $eventTitle and description: $eventDescription")

        showNotification(eventTitle, eventDescription)
        return Result.success()
    }

    private fun showNotification(title: String, description: String) {
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

        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            with(NotificationManagerCompat.from(applicationContext)) {
                Log.d("NotificationWorker", "showNotification: Sending notification with ID: $notificationId")
                notify(notificationId, builder.build())
            }
        } else {
            Log.e("NotificationWorker", "showNotification: Missing POST_NOTIFICATIONS permission")
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
                Log.d("NotificationWorker", "createNotificationChannel: Notification channel created")
            }
        }
    }
}