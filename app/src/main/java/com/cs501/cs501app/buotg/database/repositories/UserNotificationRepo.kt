package com.cs501.cs501app.buotg.database.repositories

import android.content.Context
import com.cs501.cs501app.buotg.connection.API
import com.cs501.cs501app.buotg.connection.SafeAPIRequest
import com.cs501.cs501app.buotg.database.AppDatabase
import com.cs501.cs501app.buotg.database.entities.UserNotification

class UserNotificationRepo(private val db: AppDatabase):SafeAPIRequest(){
    suspend fun pullNotifications(ctx: Context):List<UserNotification>{
        val response = apiRequest( ctx, { API.getClient().fetch_notification() })
        response?.let { db.userNotificationDao().upsertAll(it.notifications) }
        return db.userNotificationDao().pullNotifications()
    }
}