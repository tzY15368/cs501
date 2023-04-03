package com.cs501.cs501app.buotg.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/*
https://github.com/RickWayne1125/bu-on-the-go-backend/blob/master/app/models.py
 */

@Entity(tableName = "event")
data class Event(
    @PrimaryKey val event_id: Int,
    val event_name: String,
    val latitude: Long,
    val longitude: Long,
    val start_time: Date,
    val end_time: Date,
    val repeat_mode: Int,
    val priority: Int,
    val desc: String
)