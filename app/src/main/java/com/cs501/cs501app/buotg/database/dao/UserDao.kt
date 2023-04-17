package com.cs501.cs501app.buotg.database.dao

import androidx.room.*
import com.cs501.cs501app.buotg.database.entities.CURRENT_USER_KEY
import com.cs501.cs501app.buotg.database.entities.User
import java.util.UUID

@Dao
interface UserDao{
    @Query("SELECT * from user where user_id=(:id)")
    suspend fun getUser(id:UUID): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(user : User) : Long

    @Query("SELECT * FROM user WHERE user_id = (SELECT value FROM kv_entry WHERE key = (:userKey)) limit 1")
    //@Query("SELECT * FROM USER ORDER BY CREATED_AT DESC LIMIT 1")
    suspend fun getCurrentUser(userKey: String = CURRENT_USER_KEY) : User?
    // param: userKey = CURRENT_USER_KEY

    @Query("DELETE FROM kv_entry WHERE key = (:userKey)")
    suspend fun logout(userKey:String= CURRENT_USER_KEY)

    @Query("SELECT * FROM user")
    suspend fun listUsers(): List<User>

    @Upsert
    suspend fun upsertAll(users: List<User>)

    @Update
    suspend fun updateUser(user: User)
}