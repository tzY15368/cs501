package com.cs501.cs501app.buotg.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kv_entry")
data class KVEntry(
    @PrimaryKey val key: String,
    val value: String
)