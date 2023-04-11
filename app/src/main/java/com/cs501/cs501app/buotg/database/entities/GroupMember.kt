package com.cs501.cs501app.buotg.database.entities

import androidx.room.Entity
import java.util.UUID

@Entity(tableName = "group_member", primaryKeys = ["group_id", "user_id"])
data class GroupMember(
    val group_id: Int,
    val user_id: UUID
)