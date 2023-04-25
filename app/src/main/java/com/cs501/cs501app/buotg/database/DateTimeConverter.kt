package com.cs501.cs501app.buotg.database

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class DateTimeConverter {
    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
    @TypeConverter
    fun toDate(millisSinceEpoch: Long): Date {
        return Date(millisSinceEpoch)
    }

    @TypeConverter
    fun fromDateToISO(date: Date): String {
        val zoneId = ZoneId.systemDefault()
        val zonedDateTime = date.toInstant().atZone(zoneId)
        return zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }

    @TypeConverter
    fun fromISOStringToDate(isoString: String): Date {
        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val localDateTime = LocalDateTime.parse(isoString, formatter)
        val zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault())
        return Date.from(zonedDateTime.toInstant())
    }
}

