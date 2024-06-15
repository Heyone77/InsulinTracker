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
import androidx.room.TypeConverters
import androidx.room.Update
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val time: String,
    val note: String,
    val workId: String? = null
)


@Entity(tableName = "meal_entry")
data class MealEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dateUnix: Long,
    val mealTimeInt: Int,
    val stSk: Double,
    val otrabotkaSk: Double,
    val fchi: Double,
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

@Dao
interface EventDao {
    @Query("SELECT * FROM events")
    fun getAllEvents(): Flow<List<Event>>

    @Insert
    suspend fun insertEvent(event: Event): Long

    @Query("DELETE FROM events WHERE id = :eventId")
    suspend fun deleteEvent(eventId: Int)

    @Query("SELECT workId FROM events WHERE id = :eventId")
    suspend fun getEventWorkId(eventId: Int): String?

    @Query("UPDATE events SET workId = :workId WHERE id = :eventId")
    suspend fun updateEventWorkId(eventId: Int, workId: String?)
}


@Database(entities = [MealEntry::class, Event::class], version = 5, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MealDatabase : RoomDatabase() {
    abstract fun mealEntryDao(): MealEntryDao
    abstract fun eventDao(): EventDao

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
                    .addMigrations(MIGRATION_4_5)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE events ADD COLUMN workId TEXT")
            }
        }
    }
}