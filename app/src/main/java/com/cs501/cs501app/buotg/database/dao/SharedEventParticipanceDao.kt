package com.cs501.cs501app.buotg.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.cs501.cs501app.buotg.database.entities.SharedEventParticipance

@Dao
interface SharedEventParticipanceDao {
    @Query("SELECT * FROM shared_event_participance")
    suspend fun listSharedEventParticipances(): List<SharedEventParticipance>

    @Upsert
    suspend fun upsertAll(sharedEventParticipances: List<SharedEventParticipance>)
}