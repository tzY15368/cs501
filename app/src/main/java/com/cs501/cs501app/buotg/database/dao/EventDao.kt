package com.cs501.cs501app.buotg.database.dao

import androidx.room.*
import com.cs501.cs501app.buotg.database.entities.Event
import com.cs501.cs501app.buotg.database.entities.Group
import kotlinx.coroutines.flow.Flow
import java.util.*

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(event : Event)

//    @Query("SELECT * FROM event WHERE created_by = :userId")
//    suspend fun getAllEventsByUserId(userId : UUID): List<Event>
    // get all events whose start_time <= Date, end_time >= Date, and (date-start_time) % repeat_mode == 0
    @Query("SELECT * FROM event WHERE start_time <= :date AND end_time >= :date AND (julianday(:date) - julianday(start_time)) % repeat_mode = 0")
    suspend fun getAllEventsByDate(date : Date): List<Event>
}