package com.heysoft.insulintracker.data.di

import android.content.Context
import androidx.room.Room
import com.heysoft.insulintracker.data.db.EventDao
import com.heysoft.insulintracker.data.db.MealDatabase
import com.heysoft.insulintracker.data.db.MealEntryDao
import com.heysoft.insulintracker.data.repository.EventRepository
import com.heysoft.insulintracker.data.repository.MealEntryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MealDatabase {
        return Room.databaseBuilder(
            context,
            MealDatabase::class.java,
            "meal_database"
        )
            .addMigrations(MealDatabase.MIGRATION_4_5)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideMealEntryDao(database: MealDatabase): MealEntryDao {
        return database.mealEntryDao()
    }

    @Provides
    fun provideEventDao(database: MealDatabase): EventDao {
        return database.eventDao()
    }

    @Provides
    @Singleton
    fun provideMealEntryRepository(mealEntryDao: MealEntryDao): MealEntryRepository {
        return MealEntryRepository(mealEntryDao)
    }

    @Provides
    @Singleton
    fun provideEventRepository(eventDao: EventDao): EventRepository {
        return EventRepository(eventDao)
    }
}
