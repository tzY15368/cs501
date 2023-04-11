package com.cs501.cs501app.buotg.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(tableName = "group")
data class Group(
    @PrimaryKey val group_id: Int,
    val group_name: String,
    val desc: String,
    val created_at: Date,
    val owner_id: UUID
)
