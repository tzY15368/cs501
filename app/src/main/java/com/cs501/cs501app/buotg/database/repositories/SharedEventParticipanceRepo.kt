package com.cs501.cs501app.buotg.database.repositories

import android.content.Context
import com.cs501.cs501app.buotg.connection.API
import com.cs501.cs501app.buotg.connection.SEPListItem
import com.cs501.cs501app.buotg.connection.SEPsResponse
import com.cs501.cs501app.buotg.connection.SafeAPIRequest
import com.cs501.cs501app.buotg.database.AppDatabase
import com.cs501.cs501app.buotg.database.entities.SharedEventParticipance

class SharedEventParticipanceRepo(
    private val db: AppDatabase
): SafeAPIRequest() {
    suspend fun getSharedEventParticipanceBySharedEventId(ctx: Context, shared_event_id: Int):List<SEPListItem>{
        val res = apiRequest(ctx, {API.getClient().shared_event_participance_list(shared_event_id)})
        if (res != null) {
            return res.result
        } else {
            return emptyList()
        }
    }

//    suspend fun getSharedEventParticipanceStatus(shared_event_id: Int, userId: UUID): SharedEventParticipance =
//        db.sharedEventParticipanceDao().getSharedEventParticipance(shared_event_id,userId)

    suspend fun putParticipance(participance: SharedEventParticipance, ctx: Context)  {
        val res = apiRequest(ctx, {
            API.getClient().put_shared_event_participance(
                participance.shared_event_id,
                participance.user_id,
                participance.status
            )
        })
//        res?.let { db.sharedEventParticipanceDao().upsertAll(listOf(participance))}
    }


    suspend fun deleteParticipance(participance: SharedEventParticipance, ctx: Context) : SEPsResponse? {
        val res = apiRequest(ctx, { API.getClient().delete_shared_event_participance(participance.shared_event_id,participance.user_id)})
        //res?.let { db.sharedEventParticipanceDao().deleteParticipance(participance.shared_event_id, participance.user_id)}
        return res
    }

}