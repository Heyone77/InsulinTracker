package com.heysoft.insulintracker

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import java.util.concurrent.TimeUnit


class SharedViewModel(application: Application) : AndroidViewModel(application) {

    private val _isDarkTheme = MutableLiveData<Boolean>()
    val isDarkTheme: LiveData<Boolean> = _isDarkTheme

    val fchiValue: MutableLiveData<Double> = MutableLiveData()
    val mealEntries: MutableLiveData<List<MealEntry>> = MutableLiveData()
    val isAgreementAccepted: MutableLiveData<Boolean> = MutableLiveData(false)

    private val eventDao: EventDao = MealDatabase.getDatabase(application).eventDao()
    val allEvents: Flow<List<Event>> = eventDao.getAllEvents()

    private val mealEntryDao: MealEntryDao = MealDatabase.getDatabase(application).mealEntryDao()
    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    init {
        loadPreferences()
        _isDarkTheme.value = ThemePreferences.isDarkTheme(application)
    }

    fun setDarkTheme(isDark: Boolean) {
        _isDarkTheme.value = isDark
        ThemePreferences.setDarkTheme(getApplication(), isDark)
    }

    private fun loadPreferences() {
        isAgreementAccepted.value = sharedPreferences.getBoolean("isAgreementAccepted", false)
    }

    fun acceptAgreement() {
        viewModelScope.launch {
            sharedPreferences.edit().putBoolean("isAgreementAccepted", true).apply()
            isAgreementAccepted.value = true
        }
    }

    fun loadMealEntries() {
        viewModelScope.launch {
            mealEntries.value = mealEntryDao.getAllMealEntries()
            Log.i("SharedViewModel", "Loaded meal entries: ${mealEntries.value}")
        }
    }

    fun insertMealEntry(mealEntry: MealEntry) {
        viewModelScope.launch {
            mealEntryDao.insertMealEntry(mealEntry)
            Log.i("SharedViewModel", "Inserted meal entry: $mealEntry")
            loadMealEntries()
        }
    }

    fun updateMealEntry(mealEntry: MealEntry) {
        viewModelScope.launch {
            mealEntryDao.updateMealEntry(mealEntry)
            Log.i("SharedViewModel", "Updated meal entry: $mealEntry")
            loadMealEntries()
        }
    }

    fun deleteMealEntry(mealEntry: MealEntry) {
        viewModelScope.launch {
            mealEntryDao.deleteMealEntry(mealEntry)
            Log.i("SharedViewModel", "Deleted meal entry: $mealEntry")
            loadMealEntries()
        }
    }

    fun insertEvent(event: Event) {
        viewModelScope.launch {
            Log.d("SharedViewModel", "insertEvent: Inserting event with date: ${event.date} and time: ${event.time}")
            val eventId = eventDao.insertEvent(event).toInt()
            scheduleNotification(event, eventId)
        }
    }

    fun deleteEvent(eventId: Int) {
        viewModelScope.launch {
            Log.d("SharedViewModel", "deleteEvent: Deleting event with ID: $eventId")
            cancelScheduledNotification(eventId)
            eventDao.deleteEvent(eventId)
        }
    }

    private suspend fun scheduleNotification(event: Event, eventId: Int) {
        val timeDiff = calculateTimeDiff(event.date, event.time)
        if (timeDiff > 0) {
            Log.d("SharedViewModel", "scheduleNotification: Scheduling notification for event with date: ${event.date} and time: ${event.time} in $timeDiff milliseconds")

            val data = Data.Builder()
                .putString("eventTitle", "Напоминание")
                .putString("eventDescription", event.note)
                .putInt("eventId", eventId)
                .build()

            val notificationWork = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
                .setInputData(data)
                .build()

            WorkManager.getInstance(getApplication()).enqueue(notificationWork)
            Log.d("SharedViewModel", "scheduleNotification: Notification scheduled with Work ID: ${notificationWork.id}")

            eventDao.updateEventWorkId(eventId, notificationWork.id.toString())
        } else {
            Log.e("SharedViewModel", "scheduleNotification: Invalid time difference, notification not scheduled")
        }
    }

    private fun cancelScheduledNotification(eventId: Int) {
        viewModelScope.launch {
            val workId = eventDao.getEventWorkId(eventId)
            Log.d("SharedViewModel", "cancelScheduledNotification: Retrieved work ID: $workId for event ID: $eventId")
            if (workId != null) {
                WorkManager.getInstance(getApplication()).cancelWorkById(UUID.fromString(workId))
                Log.d("SharedViewModel", "cancelScheduledNotification: Cancelled notification with Work ID: $workId")
                eventDao.updateEventWorkId(eventId, null) // Очистить Work ID после удаления события
                Log.d("SharedViewModel", "cancelScheduledNotification: Updated event with ID: $eventId, cleared work ID")
            }
        }
    }

    private fun calculateTimeDiff(date: String, time: String): Long {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val dateTimeString = "$date $time"
        val eventDate = dateFormat.parse(dateTimeString)
        val currentTime = Date()
        return if (eventDate != null) {
            eventDate.time - currentTime.time
        } else {
            0L
        }
    }
}

