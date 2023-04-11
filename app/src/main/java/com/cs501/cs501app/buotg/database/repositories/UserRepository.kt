package com.cs501.cs501app.buotg.database.repositories

import android.content.Context
import android.util.Log
import com.cs501.cs501app.buotg.connection.API
import com.cs501.cs501app.buotg.connection.LoginResponse
import com.cs501.cs501app.buotg.connection.SafeAPIRequest
import com.cs501.cs501app.buotg.connection.SignupResponse
import com.cs501.cs501app.buotg.database.AppDatabase
import com.cs501.cs501app.buotg.database.entities.CURRENT_USER_ID
import com.cs501.cs501app.buotg.database.entities.KVEntry
import com.cs501.cs501app.buotg.database.entities.USER_TOKEN_KEY
import com.cs501.cs501app.buotg.database.entities.User

class UserRepository(
    db: AppDatabase
) : SafeAPIRequest() {
    private val userDao = db.userDao()
    private val kvDao = db.kvDao()

    suspend fun userLogin(ctx: Context, email: String, password: String): LoginResponse? {
        val res = apiRequest(ctx, {API.getClient().userLogin(email, password)})
        res?.let {
            userDao.upsert(res.user)
//            kvDao.put(KVEntry(USER_TOKEN_KEY, res.token))
            kvDao.put(KVEntry(USER_TOKEN_KEY, it.token))
            CURRENT_USER_ID = it.user.user_id
            Log.d("userLogin", "userLogin: ${it.user.user_id}")
        }
        return res
    }

    suspend fun userSignup(
        ctx: Context,
        name: String,
        email: String,
        password: String,
        user_type: String
    ): SignupResponse? {
        val res = apiRequest(ctx,{ API.getClient().userSignup(name, email, password, user_type) })
        res?.let { userDao.upsert(res.user) }
        return res
    }

    fun getCurrentUserID() = CURRENT_USER_ID!!
    suspend fun getCurrentUser():User? = userDao.getCurrentUser()

    suspend fun logout() = userDao.logout()

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

}
