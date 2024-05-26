package com.heysoft.insulintracker

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    val fchiValue: MutableLiveData<Double> = MutableLiveData()
    val mealEntries: MutableLiveData<List<MealEntry>> = MutableLiveData()
    val isAgreementAccepted: MutableLiveData<Boolean> = MutableLiveData(false)

    private val mealEntryDao: MealEntryDao = MealDatabase.getDatabase(application).mealEntryDao()
    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    init {
        loadPreferences()
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
}
