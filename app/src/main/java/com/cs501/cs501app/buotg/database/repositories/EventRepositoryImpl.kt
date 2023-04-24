package com.cs501.cs501app.buotg.database.repositories

import android.content.Context
import com.cs501.cs501app.buotg.database.dao.EventDao
import com.cs501.cs501app.buotg.database.entities.Event
import kotlinx.coroutines.flow.Flow
import java.util.*

class EventRepositoryImpl (private val dao: EventDao) : EventRepository {

    override val eventStream: Flow<List<Event>>
        get() = dao.getAllEvents()

    override suspend fun addEvent(ctx : Context, event: Event) {
        dao.insert(event)
    }

    override suspend fun deleteEvent(ctx : Context, event: Event) {
        dao.delete(event)
    }

    override suspend fun updateEvent(ctx : Context, event: Event) {
        dao.update(event)
    }

    override suspend fun upsertAll(ctx : Context, events: List<Event>) {
        dao.upsertAll(events)
    }

    override suspend fun getAllEventsByUserId(ctx : Context, userId : UUID): List<Event> {
        return dao.getAllEventsByUserId(userId)
    }
}