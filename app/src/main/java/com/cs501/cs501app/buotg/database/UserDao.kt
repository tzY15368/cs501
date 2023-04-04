package com.cs501.cs501app.buotg.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cs501.cs501app.buotg.entities.CURRENT_USER_ID
import com.cs501.cs501app.buotg.entities.User

@Dao
interface UserDao{
    @Query("SELECT * from user where user_id=(:id)")
    suspend fun getUser(id:Int): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(user : User) : Long

    @Query("SELECT * FROM user WHERE uid = $CURRENT_USER_ID")
    fun getuser() : LiveData<User>
}