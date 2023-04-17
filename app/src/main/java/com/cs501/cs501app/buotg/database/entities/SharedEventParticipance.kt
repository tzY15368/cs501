package com.cs501.cs501app.buotg.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

enum class Status {
    FAIL,
    SUCCESS
}

@Entity(tableName = "shared_event_participance", primaryKeys = ["shared_event_id", "user_id"])
data class SharedEventParticipance(
    val shared_event_id: Int,
    val user_id: UUID,
    val status: Status
)