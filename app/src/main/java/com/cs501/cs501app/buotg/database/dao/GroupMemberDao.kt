package com.cs501.cs501app.buotg.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.cs501.cs501app.buotg.database.entities.GroupMember

@Dao
interface GroupMemberDao {
    @Query("SELECT * from group_member")
    suspend fun listGroupMembers(): List<GroupMember>

    @Upsert
    suspend fun upsertAll(groupMembers: List<GroupMember>)
}