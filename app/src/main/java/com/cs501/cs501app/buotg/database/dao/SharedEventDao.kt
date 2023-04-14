package com.cs501.cs501app.buotg.database.dao

import androidx.room.*
import com.cs501.cs501app.buotg.database.entities.CURRENT_USER_KEY
import com.cs501.cs501app.buotg.database.entities.SharedEvent
import java.sql.Timestamp
import java.util.*

@Dao
interface SharedEventDao {
    @Query("SELECT * FROM shared_event")
    suspend fun listSharedEvents(): List<SharedEvent>

    @Upsert
    suspend fun upsertAll(sharedEvents: List<SharedEvent>)

    // sharedevent may not have been created by myself
    @Query("SELECT * FROM shared_event WHERE (checkin_time BETWEEN (:checkinTimeStart) AND (:checkinTimeEnd))")
    suspend fun getAllSharedEvent(
        checkinTimeStart: Date=Date(Timestamp(0).time),
        checkinTimeEnd: Date=Date(Timestamp(0x7fffffff).time),
    ): List<SharedEvent>

    // only query for shared events that current user created
    @Query("SELECT * FROM shared_event WHERE owner_id=(SELECT value FROM kv_entry WHERE key = (:userKey))")
    suspend fun getMySharedEvents(userKey: String = CURRENT_USER_KEY): List<SharedEvent>

    @Query("DELETE FROM shared_event WHERE shared_event_id=(:sharedEventId)")
    suspend fun delete(sharedEventId:Int)

    @Query("DELETE FROM shared_event_participance WHERE shared_event_id=(:share_id)")
    suspend fun deleteSharedEventParticipance(share_id:Int)

    @Transaction
    suspend fun deleteSharedEvent(share_id:Int){
        deleteSharedEventParticipance(share_id)
        delete(share_id)
    }
}