package com.cs501.cs501app.buotg.database.entities

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cs501.cs501app.R
import com.cs501.cs501app.buotg.database.DateTimeConverter
import java.util.Date
import java.util.UUID

/*
https://github.com/RickWayne1125/bu-on-the-go-backend/blob/master/app/models.py
 */

@Entity(tableName = "event")
data class Event(
    @PrimaryKey val event_id: UUID,
    val event_name: String,
    val latitude: Long,
    val longitude: Long,
    val start_time: Date,
    val end_time: Date,
    // the value of repeat_mode means that event happens every N days
    val repeat_mode: Int,
    val priority: Int,
    val desc: String,
    val notify_time: Int // notify time in minutes
)

data class ApiEvent(
    val event_id: String,
    val event_name: String,
    val latitude: Long,
    val longitude: Long,
    val start_time: String,
    val end_time: String,
    val repeat_mode: Int,
    val priority: Int,
    val desc: String,
    val notify_time: Int
)

fun apiEventToEvent(apiEvent: ApiEvent): Event {
    return Event(
        event_id = UUID.fromString(apiEvent.event_id),
        event_name = apiEvent.event_name,
        latitude = apiEvent.latitude,
        longitude = apiEvent.longitude,
        start_time = DateTimeConverter().fromISOStringToDate(apiEvent.start_time),
        end_time = DateTimeConverter().fromISOStringToDate(apiEvent.end_time),
        repeat_mode = apiEvent.repeat_mode,
        priority = apiEvent.priority,
        desc = apiEvent.desc,
        notify_time = apiEvent.notify_time
    )
}


enum class EventPriority(val color: Color, @StringRes val priority: Int) {
    Red(Color.Red, R.string.high_priority),
    Cyan(Color.Cyan, R.string.medium_priority),
    Blue(Color.Blue, R.string.low_priority),
//    Green(Color.Green, 0),
//    Cyan(Color.Cyan, 0),
//    Magenta(Color.Magenta, 0),
//    Orange(OrangeColor, 0)
}