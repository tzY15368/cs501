package com.cs501.cs501app.buotg.database.repositories

import android.content.Context
import android.util.Log
import com.cs501.cs501app.buotg.connection.API
import com.cs501.cs501app.buotg.connection.LoginResponse
import com.cs501.cs501app.buotg.connection.SafeAPIRequest
import com.cs501.cs501app.buotg.connection.SignupResponse
import com.cs501.cs501app.buotg.database.AppDatabase
import com.cs501.cs501app.buotg.database.entities.*
import java.util.UUID

class UserRepository(
    db: AppDatabase
) : SafeAPIRequest() {
    private val userDao = db.userDao()
    private val kvDao = db.kvDao()

    suspend fun userGoogleLogin(ctx: Context, token: String): LoginResponse? {
        val res = apiRequest(ctx, {API.getClient().userGoogleLogin(token)})
        res?.let {
            userDao.upsert(res.user)
            kvDao.put(KVEntry(USER_TOKEN_KEY, it.token))
            kvDao.put(KVEntry(CURRENT_USER_KEY, it.user.user_id.toString().replace("-","")))
            CURRENT_USER_ID = it.user.user_id
            Log.d("userLogin", "userLogin: ${it.user.user_id}")
        }
        return res
    }

    suspend fun userLogin(ctx: Context, email: String, password: String): LoginResponse? {
        val res = apiRequest(ctx, {API.getClient().userLogin(email, password)})
        res?.let {
            userDao.upsert(res.user)
            kvDao.put(KVEntry(USER_TOKEN_KEY, it.token))
            println("got userid: ${it.user.user_id}")
            kvDao.put(KVEntry(CURRENT_USER_KEY, it.user.user_id.toString()))
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
    suspend fun getCurrentUser():User?  {
        val u = userDao.getCurrentUser()
        println("getCurrentUser: $u")
        return u
    }

    suspend fun fetchUser(ctx: Context, id: UUID):User?{
        val u = userDao.getUser(id)
        if(u == null){
            val res = apiRequest(ctx, {API.getClient().getUser(id)})
            res?.let { userDao.upsert(it.user) }
            return res?.user
        }
        return u
    }

    suspend fun logout() {
        userDao.logout()
        CURRENT_USER_ID = null
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

}
