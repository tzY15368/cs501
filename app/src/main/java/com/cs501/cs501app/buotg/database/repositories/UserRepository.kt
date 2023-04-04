package com.cs501.cs501app.buotg.database.repositories

import com.cs501.cs501app.buotg.database.AppDatabase
import com.cs501.cs501app.buotg.database.connection.API
import com.cs501.cs501app.buotg.database.connection.AuthResponses
import com.cs501.cs501app.buotg.database.connection.SafeAPIRequest
import com.cs501.cs501app.buotg.entities.User

class UserRepository(
    private val api: API,
    private val db: AppDatabase

) : SafeAPIRequest(){

    suspend fun userLogin(email: String, password: String): AuthResponses {
        return apiRequest { api.userLogin(email, password) }
    }

    suspend fun userSignup(
        name: String,
        email: String,
        password: String
    ):AuthResponses{
        return apiRequest { api.userSignup(name,email,password) }
    }


    suspend fun saveUser(user: User) = db.userDao().upsert(user)

    fun getUser() = db.userDao().getuser()

}
