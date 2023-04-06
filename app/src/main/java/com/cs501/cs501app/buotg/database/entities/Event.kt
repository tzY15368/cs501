package com.cs501.cs501app.buotg.database.entities

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cs501.cs501app.R
import java.util.Date

/*
https://github.com/RickWayne1125/bu-on-the-go-backend/blob/master/app/models.py
 */

@Entity(tableName = "event")
data class Event(
//    @PrimaryKey val event_id: Int,
    @PrimaryKey(autoGenerate = true) val event_id: Int = 0,
    val event_name: String,
    val latitude: Long,
    val longitude: Long,
    val start_time: Date,
    val end_time: Date,
    val repeat_mode: Int,
    val priority: Int,
    val desc: String
)

enum class EventPriority(val color: Color, @StringRes val priority: Int) {
    Red(Color.Red, R.string.high_priority),
    Cyan(Color.Cyan, R.string.medium_priority),
    Blue(Color.Blue, R.string.low_priority),
//    Green(Color.Green, 0),
//    Cyan(Color.Cyan, 0),
//    Magenta(Color.Magenta, 0),
//    Orange(OrangeColor, 0)
}