package com.cs501.cs501app.buotg.database.repositories

import com.cs501.cs501app.buotg.connection.API
import com.cs501.cs501app.buotg.connection.LoginResponse
import com.cs501.cs501app.buotg.connection.SafeAPIRequest
import com.cs501.cs501app.buotg.connection.SignupResponse
import com.cs501.cs501app.buotg.database.AppDatabase
import com.cs501.cs501app.buotg.connection.*
import com.cs501.cs501app.buotg.entities.KVEntry
import com.cs501.cs501app.buotg.entities.USER_TOKEN_KEY
import com.cs501.cs501app.buotg.entities.User

class UserRepository(
    db: AppDatabase
) : SafeAPIRequest() {
    private val api: API = API.getClient()
    private val userDao = db.userDao()
    private val kvDao = db.kvDao()

    suspend fun userLogin(email: String, password: String): LoginResponse {
        val res = apiRequest { api.userLogin(email, password) }
        kvDao.put(KVEntry(USER_TOKEN_KEY, res.token))

        return res
    }

    suspend fun userSignup(
        name: String,
        email: String,
        password: String,
        user_type: String
    ): SignupResponse {
        val res = apiRequest { api.userSignup(name, email, password, user_type) }
        assert(res.user.user_id != 0)
        userDao.upsert(res.user)
        return res
    }

    fun getCurrentUser() = userDao.getCurrentUser()

    fun logout() = userDao.logout()
}
