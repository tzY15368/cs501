package com.cs501.cs501app.buotg.database.repositories

import android.content.Context
import com.cs501.cs501app.buotg.connection.API
import com.cs501.cs501app.buotg.connection.SafeAPIRequest
import com.cs501.cs501app.buotg.connection.SharedEventListResponse
import com.cs501.cs501app.buotg.connection.SharedEventResponse
import com.cs501.cs501app.buotg.database.AppDatabase
import com.cs501.cs501app.buotg.database.entities.SharedEvent
import java.sql.Timestamp
import java.util.*

class SharedEventRepo(
    private val db: AppDatabase
) : SafeAPIRequest() {
    suspend fun getAllSharedEvent(
        checkinTimeStart: Date = Date(Timestamp(0).time),
        checkinTimeEnd: Date = Date(Timestamp(0x7fffffff).time)
    ):List<SharedEvent> = db.sharedEventDao().getAllSharedEvent()

    suspend fun getAllSharedEventByEventId(
        eventId : UUID,
        ctx: Context
    ): SharedEventListResponse? {

        val res = apiRequest(ctx, { API.getClient().get_shared_event()})
        res?.let { db.sharedEventDao().getAllSharedEventByEventId(eventId)        }
        return res
    }

    suspend fun updateSharedEvent(
        sharedEvent: SharedEvent,
        ctx: Context
    ) : SharedEventListResponse? {
        val res = apiRequest(ctx, { API.getClient().get_shared_event()})
        res?.let { db.sharedEventDao().upsertAll(listOf(sharedEvent))   }

        return res
    }


    suspend fun deleteSharedEvent(sharedEventId: Int) = db.sharedEventDao().deleteSharedEvent(sharedEventId)
    suspend fun getMySharedEvents():List<SharedEvent> = db.sharedEventDao().getMySharedEvents()
}