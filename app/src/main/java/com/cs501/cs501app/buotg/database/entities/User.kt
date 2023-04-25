package com.cs501.cs501app.buotg.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

const val CURRENT_USER_KEY = "current_user_id"
const val USER_TOKEN_KEY = "user_token"
const val USER_LATITUDE_VAL_FROM= 42.3398
const val USER_LONGITUDE_VAL_FROM = -71.0892
const val USER_LATITUDE_VAL_TO= 42.3496
const val USER_LONGITUDE_VAL_TO = -71.0997
var CURRENT_USER_ID: UUID? = null
enum class UserType{
    student,
    teacher,
    staff
}

@Entity(tableName = "user")
data class User(
    @PrimaryKey val user_id:UUID,
    val email: String,
    val full_name:String,
    val created_at:Date,
    val user_type: UserType
)
