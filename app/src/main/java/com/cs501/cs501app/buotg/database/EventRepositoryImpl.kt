package com.cs501.cs501app.buotg.database

import com.cs501.cs501app.buotg.entities.Event
import kotlinx.coroutines.flow.Flow

class EventRepositoryImpl (private val dao: EventDao) : EventRepository {
    override val eventStream: Flow<List<Event>>
        get() = dao.getAllEvents()

    override suspend fun addEvent(event: Event) {
        dao.insert(event)
    }

    override suspend fun deleteEvent(event: Event) {
        dao.delete(event)
    }

    override suspend fun updateEvent(event: Event) {
        dao.update(event)
    }
}