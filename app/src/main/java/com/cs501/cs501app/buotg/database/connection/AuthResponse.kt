package com.cs501.cs501app.buotg.database.connection

import com.cs501.cs501app.buotg.entities.User

data class AuthResponses(

    val isSuccessful : Boolean?,
    val message : String?,
    val user : User?
)
