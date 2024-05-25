package com.heysoft.insulintracker


import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

@Entity(tableName = "meal_entry")
data class MealEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dateUnix: Long,
    val mealTimeInt: Int,
    val uk: Double
)


@Dao
interface MealEntryDao {
    @Insert
    suspend fun insertMealEntry(mealEntry: MealEntry)

    @Query("SELECT * FROM meal_entry")
    suspend fun getAllMealEntries(): List<MealEntry>

    @Query("DELETE FROM meal_entry")
    suspend fun deleteAllMealEntries()
}


@Database(entities = [MealEntry::class], version = 1, exportSchema = false)
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
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}