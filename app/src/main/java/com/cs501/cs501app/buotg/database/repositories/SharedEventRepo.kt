package com.cs501.cs501app.buotg.database.repositories

import android.content.Context
import android.util.Log
import com.cs501.cs501app.buotg.connection.*
import com.cs501.cs501app.buotg.database.AppDatabase
import com.cs501.cs501app.buotg.database.UUIDConverter
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

        val res = apiRequest(ctx, { API.getClient().get_shared_event(UUIDConverter.fromUUID(eventId))}) // TODO: wait for backend
        Log.d("SharedEventRepo", "getAllSharedEventByEventId: $res")
        res?.let { db.sharedEventDao().upsertAll(it.shared_event) }
        return res
    }

    suspend fun updateSharedEvent(
        sharedEvent: SharedEvent,
        ctx: Context
    ) : StdResponse? {
        Log.d("SharedEventRepo", "updateSharedEvent: $sharedEvent")
        val res = apiRequest(ctx, { API.getClient().create_shared_event(UUIDConverter.fromUUID(sharedEvent.event_id))}) //TODO: wait for backend
        Log.d("SharedEventRepo", "updateSharedEvent: $res")
        res?.let { db.sharedEventDao().upsertAll(listOf(sharedEvent)) }
        return res
    }


    suspend fun deleteSharedEvent(ctx : Context, sharedEventId: Int) {
        val res = apiRequest(ctx, { API.getClient().delete_shared_event(sharedEventId) })
        db.sharedEventDao().deleteSharedEvent(sharedEventId)
    }
    suspend fun getMySharedEvents():List<SharedEvent> = db.sharedEventDao().getMySharedEvents()
}