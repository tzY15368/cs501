package com.cs501.cs501app.buotg.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
const val CURRENT_USER_KEY = "current_user_id"
const val USER_TOKEN_KEY = "user_token"
enum class UserType{
    student,
    teacher,
    staff
}

@Entity(tableName = "user")
data class User(
    @PrimaryKey val user_id:Int,
    val email: String,
    val full_name:String,
    val created_at:Date,
    val user_type: UserType
)
