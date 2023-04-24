package com.cs501.cs501app.buotg.database.repositories

import android.content.Context
import com.cs501.cs501app.buotg.database.entities.Event
import kotlinx.coroutines.flow.Flow
import java.util.UUID

/**
 * Interface for [EventRepository] which contains method to access and modify items
 */
interface EventRepository {
    val eventStream: Flow<List<Event>>

    suspend fun addEvent(ctx : Context, event: Event)

    suspend fun deleteEvent(ctx : Context, event: Event)

    suspend fun updateEvent(ctx : Context, event: Event)

    suspend fun upsertAll(ctx : Context, events: List<Event>)

    suspend fun getAllEventsByUserId(ctx : Context, userId : UUID): List<Event>
}