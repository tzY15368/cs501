package com.cs501.cs501app.buotg.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cs501.cs501app.buotg.entities.KVEntry

@Dao
interface KVDao {
    @Query("SELECT * FROM kv_entry WHERE key = (:key)")
    suspend fun get(key: String): KVEntry?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun put(kv: KVEntry)

    @Query("DELETE FROM kv_entry WHERE key = (:key)")
    suspend fun delete(key: String)
}