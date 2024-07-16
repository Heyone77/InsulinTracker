package com.heysoft.insulintracker.viewmodel

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.heysoft.insulintracker.data.db.Event
import com.heysoft.insulintracker.data.db.EventDao
import com.heysoft.insulintracker.data.db.MealEntry
import com.heysoft.insulintracker.data.db.MealEntryDao
import com.heysoft.insulintracker.ui.theme.ThemePreferences
import com.heysoft.insulintracker.utils.cancelScheduledNotification
import com.heysoft.insulintracker.utils.scheduleNotification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    application: Application,
    private val mealEntryDao: MealEntryDao,
    private val eventDao: EventDao,
    private val sharedPreferences: SharedPreferences
) : AndroidViewModel(application) {

    private val _isDarkTheme = MutableStateFlow(ThemePreferences.isDarkTheme(application))
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    init {
        sharedPreferences.registerOnSharedPreferenceChangeListener { _, key ->
            if (key == ThemePreferences.KEY_IS_DARK_THEME) {
                _isDarkTheme.value = ThemePreferences.isDarkTheme(application)
            }
        }
    }

    fun setDarkTheme(isDark: Boolean) {
        Log.d("SharedViewModel", "setDarkTheme: Setting theme to ${if (isDark) "dark" else "light"}")
        ThemePreferences.setDarkTheme(getApplication(), isDark)
        _isDarkTheme.value = isDark
        Log.d("SharedViewModel", "setDarkTheme: Theme set in preferences")
    }

    // Остальной код
    val fchiValue = MutableStateFlow(0.0)
    val mealEntries = MutableStateFlow(emptyList<MealEntry>())
    val isAgreementAccepted =
        MutableStateFlow(sharedPreferences.getBoolean("isAgreementAccepted", false))

    val allEvents: Flow<List<Event>> = eventDao.getAllEvents()

    fun acceptAgreement() {
        sharedPreferences.edit().putBoolean("isAgreementAccepted", true).apply()
        isAgreementAccepted.value = true
    }

    fun loadMealEntries() {
        viewModelScope.launch {
            mealEntries.value = mealEntryDao.getAllMealEntries()
        }
    }

    fun insertMealEntry(mealEntry: MealEntry) {
        viewModelScope.launch {
            mealEntryDao.insertMealEntry(mealEntry)
            loadMealEntries()
        }
    }

    fun updateMealEntry(mealEntry: MealEntry) {
        viewModelScope.launch {
            mealEntryDao.updateMealEntry(mealEntry)
            loadMealEntries()
        }
    }

    fun deleteMealEntry(mealEntry: MealEntry) {
        viewModelScope.launch {
            mealEntryDao.deleteMealEntry(mealEntry)
            loadMealEntries()
        }
    }

    fun insertEvent(event: Event) {
        viewModelScope.launch {
            val eventId = eventDao.insertEvent(event).toInt()
            scheduleNotification(getApplication(), event, eventId, eventDao)
        }
    }

    fun deleteEvent(eventId: Int) {
        viewModelScope.launch {
            cancelScheduledNotification(getApplication(), eventId, eventDao)
            eventDao.deleteEvent(eventId)
        }
    }
}
