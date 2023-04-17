package com.cs501.cs501app.buotg.database.repositories

import com.cs501.cs501app.buotg.connection.SafeAPIRequest
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

    suspend fun updateSharedEvent(sharedEvent: SharedEvent) =
        db.sharedEventDao().upsertAll(listOf(sharedEvent))

    suspend fun deleteSharedEvent(sharedEventId: Int) = db.sharedEventDao().deleteSharedEvent(sharedEventId)
    suspend fun getMySharedEvents():List<SharedEvent> = db.sharedEventDao().getMySharedEvents()
}