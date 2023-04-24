package com.cs501.cs501app.buotg.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.cs501.cs501app.buotg.database.entities.GroupMember

@Dao
interface GroupMemberDao {
    @Query("SELECT * from group_member")
    suspend fun listGroupMembers(): List<GroupMember>

    @Query("SELECT * from group_member WHERE group_id=(:group_id)")
    suspend fun getGroupMembersById(group_id: Int): List<GroupMember>
    @Upsert
    suspend fun upsertAll(groupMembers: List<GroupMember>)
}