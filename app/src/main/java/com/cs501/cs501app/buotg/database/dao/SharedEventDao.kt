package com.cs501.cs501app.buotg.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.cs501.cs501app.buotg.database.entities.SharedEvent

@Dao
interface SharedEventDao {
    @Query("SELECT * FROM shared_event")
    suspend fun listSharedEvents(): List<SharedEvent>

    @Upsert
    suspend fun upsertAll(sharedEvents: List<SharedEvent>)
}