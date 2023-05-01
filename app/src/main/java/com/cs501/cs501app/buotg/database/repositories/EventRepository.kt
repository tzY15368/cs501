package com.cs501.cs501app.buotg.database.repositories

import android.content.Context
import android.location.Location
import android.util.Log
import com.cs501.cs501app.buotg.connection.*
import com.cs501.cs501app.buotg.database.AppDatabase
import com.cs501.cs501app.buotg.database.DateTimeConverter
import com.cs501.cs501app.buotg.database.UUIDConverter
import com.cs501.cs501app.buotg.database.entities.Event
import java.util.*

class EventRepository (db: AppDatabase) : SafeAPIRequest() {
    private val dao = db.eventDao()

    suspend fun deleteEvent(ctx : Context, event: Event): StdResponse? {
        val res = apiRequest(ctx, { API.getClient().delete_event(UUIDConverter.fromUUID(event.event_id)) })
        dao.delete(event)
        return res
    }

    suspend fun upsertEvent(ctx : Context, event: Event) {
        Log.d("EventRepository", "upsertEvent: $event")
        val zonedStartDateTime = DateTimeConverter().fromDateToISO(event.start_time)
        val zonedEndDateTime = DateTimeConverter().fromDateToISO(event.end_time)
        val res = apiRequest(ctx, { API.getClient().create_event(UUIDConverter.fromUUID(event.event_id), event.event_name, event.latitude, event.longitude,
                                    zonedStartDateTime, zonedEndDateTime, event.repeat_mode, event.priority, event.desc) })
        dao.upsert(event)
    }

    suspend fun getEventById(ctx : Context, eventId : UUID): EventResponse? {
        val res = apiRequest(ctx, { API.getClient().event_detail(UUIDConverter.fromUUID(eventId)) })
        return res
    }

    suspend fun upsertAll(ctx : Context, events: List<Event>) {
        dao.upsertAll(events)
    }

//    suspend fun getAllEventsByUserId(ctx : Context, userId : UUID): List<Event> {
//        Log.d("EventRepository", "getAllEventsByUserId: $userId")
//        return dao.getAllEventsByUserId(userId)
//    }

    suspend fun listEvents(ctx:Context): EventsResponse? {
        val res = apiRequest(ctx, {API.getClient().event_list()})
        Log.d("EventRepository", "listEvents: $res")
        if (res != null) {
            if (res.events != null) {
                Log.d("EventRepository", "listEvents: ${res.events}")
//                res.events.map { apiEventToEvent(it) } ?: emptyList()
                dao.upsertAll(res.events)
            }
        }
        Log.d("EventRepository", "listEvents")
//        res?.let { dao.upsertAll(it.Events) }
        return res
    }


}