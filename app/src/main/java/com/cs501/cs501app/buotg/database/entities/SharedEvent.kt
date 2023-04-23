package com.cs501.cs501app.buotg.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/*
https://github.com/RickWayne1125/bu-on-the-go-backend/blob/master/app/models.py
 */

// we don't care who's in that sharedevent, if user has this shared event in his db,
// the user is expected to show up

@Entity(tableName = "shared_event")
data class SharedEvent(
    @PrimaryKey val shared_event_id: Int,
    val event_id: UUID,
    val owner_id: UUID,
    val created_at: Date,
    val checkin_time: Date?
)