package com.cs501.cs501app.buotg.database.dao

import androidx.room.*
import com.cs501.cs501app.buotg.database.entities.Event
import com.cs501.cs501app.buotg.database.entities.Group
import com.cs501.cs501app.buotg.database.entities.User
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {
    @Query("SELECT * from `group`")
    fun listGroups(): List<Group>
    //add new group
    @Insert
    suspend fun insert(group: Group)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(group : Group)
    @Upsert
    suspend fun upsertAll(groups: List<Group>)
}