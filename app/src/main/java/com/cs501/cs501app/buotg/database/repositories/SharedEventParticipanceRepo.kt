package com.cs501.cs501app.buotg.database.repositories

import android.content.Context
import android.util.Log
import com.cs501.cs501app.buotg.connection.API
import com.cs501.cs501app.buotg.connection.SEPsResponse
import com.cs501.cs501app.buotg.connection.SafeAPIRequest
import com.cs501.cs501app.buotg.database.AppDatabase
import com.cs501.cs501app.buotg.database.entities.SharedEventParticipance
import com.cs501.cs501app.buotg.database.entities.User
import java.util.UUID

class SharedEventParticipanceRepo(
    private val db: AppDatabase
): SafeAPIRequest() {
    suspend fun getSharedEventParticipanceBySharedEventId(shared_event_id: Int):List<User> =
        db.sharedEventParticipanceDao().getSharedEventParticipants(shared_event_id)

    suspend fun getSharedEventParticipanceStatus(shared_event_id: Int, userId: UUID): SharedEventParticipance =
        db.sharedEventParticipanceDao().getSharedEventParticipance(shared_event_id,userId)

    suspend fun updateParticipance(participance: SharedEventParticipance, ctx: Context) : SEPsResponse? {
        val res = apiRequest(ctx, { API.getClient().create_shared_event_participance(participance.shared_event_id,participance.user_id,participance.status)})
//        res?.let { db.sharedEventParticipanceDao().upsertAll(listOf(participance))}
        return res
    }


    suspend fun deleteParticipance(participance: SharedEventParticipance, ctx: Context) : SEPsResponse? {
        val res = apiRequest(ctx, { API.getClient().delete_shared_event_participance(participance.shared_event_id,participance.user_id)})
        res?.let { db.sharedEventParticipanceDao().deleteParticipance(participance.shared_event_id, participance.user_id)}
        return res
    }

}