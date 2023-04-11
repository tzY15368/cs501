package com.cs501.cs501app.buotg.database.dao

import androidx.room.*
import com.cs501.cs501app.buotg.database.entities.Event
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Query("SELECT * from event")
    fun getAllEvents(): Flow<List<Event>>

    @Insert
    suspend fun insert(event: Event)

    @Delete
    suspend fun delete(event: Event)

    @Update
    suspend fun update(event: Event)

    @Query("SELECT * FROM event")
    suspend fun listEvents(): List<Event>

    @Upsert
    suspend fun upsertAll(events: List<Event>)
}