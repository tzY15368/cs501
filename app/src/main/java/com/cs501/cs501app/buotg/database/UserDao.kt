package com.cs501.cs501app.buotg.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cs501.cs501app.buotg.entities.CURRENT_USER_KEY
import com.cs501.cs501app.buotg.entities.User

@Dao
interface UserDao{
    @Query("SELECT * from user where user_id=(:id)")
    suspend fun getUser(id:Int): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(user : User) : Long

    @Query("SELECT * FROM user WHERE user_id = (SELECT value FROM kv_entry WHERE key = (:userKey)) limit 1")
    fun getCurrentUser(userKey:String= CURRENT_USER_KEY) : User?

    @Query("DELETE FROM kv_entry WHERE key = (:userKey)")
    fun logout(userKey:String= CURRENT_USER_KEY)
}