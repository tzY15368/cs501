package com.cs501.cs501app.buotg.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.cs501.cs501app.buotg.database.entities.SharedEventParticipance
import com.cs501.cs501app.buotg.database.entities.User
import java.util.*

@Dao
interface SharedEventParticipanceDao {
    @Query("SELECT * FROM shared_event_participance")
    suspend fun listSharedEventParticipances(): List<SharedEventParticipance>

    @Upsert
    suspend fun upsertAll(sharedEventParticipances: List<SharedEventParticipance>)

    @Query("SELECT * FROM user WHERE user_id IN (SELECT user_id FROM shared_event_participance WHERE shared_event_id = (:shared_event_id))")
    suspend fun getSharedEventParticipants(shared_event_id: Int): List<User>

    @Query("SELECT * FROM shared_event_participance WHERE shared_event_id=(:shared_event_id) AND user_id=(:user_id)")
    suspend fun getSharedEventParticipance(shared_event_id: Int, user_id: UUID): SharedEventParticipance

    @Query("DELETE FROM shared_event_participance WHERE shared_event_id=(:shared_event_id) AND user_id=(:user_id)")
    suspend fun deleteParticipance(shared_event_id: Int, user_id: UUID)
}