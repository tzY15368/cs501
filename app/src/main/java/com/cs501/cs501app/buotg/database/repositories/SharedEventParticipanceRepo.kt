package com.cs501.cs501app.buotg.database.repositories

import com.cs501.cs501app.buotg.connection.SafeAPIRequest
import com.cs501.cs501app.buotg.database.AppDatabase
import com.cs501.cs501app.buotg.database.entities.SharedEventParticipance
import com.cs501.cs501app.buotg.database.entities.User

class SharedEventParticipanceRepo(
    private val db: AppDatabase
): SafeAPIRequest() {
    suspend fun getSharedEventParticipanceBySharedEventId(shared_event_id: Int):List<User> =
        db.sharedEventParticipanceDao().getSharedEventParticipants(shared_event_id)

    suspend fun updateParticipance(participance: SharedEventParticipance) =
        db.sharedEventParticipanceDao().upsertAll(listOf(participance))

    suspend fun deleteParticipance(participance: SharedEventParticipance) =
        db.sharedEventParticipanceDao().deleteParticipance(participance.shared_event_id, participance.user_id)
}