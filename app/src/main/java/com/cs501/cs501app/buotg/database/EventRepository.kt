package com.cs501.cs501app.buotg.database

import com.cs501.cs501app.buotg.entities.Event
import kotlinx.coroutines.flow.Flow

/**
 * Interface for [EventRepository] which contains method to access and modify juice items
 */
interface EventRepository {
    val eventStream: Flow<List<Event>>

    suspend fun addEvent(event: Event)

    suspend fun deleteEvent(event: Event)

    suspend fun updateEvent(event: Event)
}