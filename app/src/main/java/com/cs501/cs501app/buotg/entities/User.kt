package com.cs501.cs501app.buotg.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

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
