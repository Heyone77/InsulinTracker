package com.heysoft.insulintracker


import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update

@Entity(tableName = "meal_entry")
data class MealEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dateUnix: Long,
    val mealTimeInt: Int,
    val stSk: Double,
    val otrabotkaSk: Double,
    val fchi: Int,
    val doz: Double,
    val carbs: Double,
    val uk: Double
)


@Dao
interface MealEntryDao {
    @Insert
    suspend fun insertMealEntry(mealEntry: MealEntry)

    @Update
    suspend fun updateMealEntry(mealEntry: MealEntry)

    @Delete
    suspend fun deleteMealEntry(mealEntry: MealEntry)

    @Query("SELECT * FROM meal_entry")
    suspend fun getAllMealEntries(): List<MealEntry>

    @Query("DELETE FROM meal_entry")
    suspend fun deleteAllMealEntries()
}


@Database(entities = [MealEntry::class], version = 2, exportSchema = false)
abstract class MealDatabase : RoomDatabase() {
    abstract fun mealEntryDao(): MealEntryDao

    companion object {
        @Volatile
        private var INSTANCE: MealDatabase? = null

        fun getDatabase(context: Context): MealDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MealDatabase::class.java,
                    "meal_database"
                )
                    .fallbackToDestructiveMigration() // Добавим это, чтобы база данных пересоздавалась при изменении схемы
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}