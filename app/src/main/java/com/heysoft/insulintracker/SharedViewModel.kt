package com.heysoft.insulintracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    val fchiValue: MutableLiveData<Double> = MutableLiveData()

    private val mealEntryDao = MealDatabase.getDatabase(application).mealEntryDao()


    private val _mealEntries = MutableLiveData<List<MealEntry>>()
    val mealEntries: LiveData<List<MealEntry>> get() = _mealEntries

    init {
        loadMealEntries()
    }

    fun insertMealEntry(mealEntry: MealEntry) {
        viewModelScope.launch(Dispatchers.IO) {
            mealEntryDao.insertMealEntry(mealEntry)
            loadMealEntries()
        }
    }

    fun loadMealEntries() {
        viewModelScope.launch(Dispatchers.IO) {
            _mealEntries.postValue(mealEntryDao.getAllMealEntries())
        }
    }

    fun deleteAllMealEntries() {
        viewModelScope.launch(Dispatchers.IO) {
            mealEntryDao.deleteAllMealEntries()
            loadMealEntries()
        }
    }

}

