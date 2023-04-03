package com.cs501.cs501app.buotg.database

import androidx.room.Dao
import androidx.room.Query
import com.cs501.cs501app.buotg.entities.User

@Dao
interface UserDao{
    @Query("SELECT * from user where id={:id}")
    suspend fun getUser(id:Int): User?
}