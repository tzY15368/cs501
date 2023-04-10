package com.cs501.cs501app.buotg.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.cs501.cs501app.buotg.database.entities.Group

@Dao
interface GroupDao {
    @Query("SELECT * from `group`")
    suspend fun listGroups(): List<Group>

    @Upsert
    suspend fun upsertAll(groups: List<Group>)
}