package com.heysoft.insulintracker.data.repository

import com.heysoft.insulintracker.data.db.MealEntry
import com.heysoft.insulintracker.data.db.MealEntryDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MealEntryRepository @Inject constructor(private val mealEntryDao: MealEntryDao) {

    suspend fun insertMealEntry(mealEntry: MealEntry) {
        mealEntryDao.insertMealEntry(mealEntry)
    }

    suspend fun updateMealEntry(mealEntry: MealEntry) {
        mealEntryDao.updateMealEntry(mealEntry)
    }

    suspend fun deleteMealEntry(mealEntry: MealEntry) {
        mealEntryDao.deleteMealEntry(mealEntry)
    }

    suspend fun getAllMealEntries(): List<MealEntry> {
        return mealEntryDao.getAllMealEntries()
    }

    suspend fun deleteAllMealEntries() {
        mealEntryDao.deleteAllMealEntries()
    }
}
