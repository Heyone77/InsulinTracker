package com.heysoft.insulintracker.data.repository

import com.heysoft.insulintracker.data.db.Event
import com.heysoft.insulintracker.data.db.EventDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepository @Inject constructor(private val eventDao: EventDao) {

    fun getAllEvents(): Flow<List<Event>> {
        return eventDao.getAllEvents()
    }

    suspend fun insertEvent(event: Event): Long {
        return eventDao.insertEvent(event)
    }

    suspend fun deleteEvent(eventId: Int) {
        eventDao.deleteEvent(eventId)
    }

    suspend fun getEventWorkId(eventId: Int): String? {
        return eventDao.getEventWorkId(eventId)
    }

    suspend fun updateEventWorkId(eventId: Int, workId: String?) {
        eventDao.updateEventWorkId(eventId, workId)
    }
}
