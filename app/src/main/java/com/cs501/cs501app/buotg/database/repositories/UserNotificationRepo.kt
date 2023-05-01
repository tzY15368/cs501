package com.cs501.cs501app.buotg.database.repositories

import android.content.Context
import com.cs501.cs501app.buotg.connection.API
import com.cs501.cs501app.buotg.connection.SafeAPIRequest
import com.cs501.cs501app.buotg.database.AppDatabase
import com.cs501.cs501app.buotg.database.entities.NOTI_Status
import com.cs501.cs501app.buotg.database.entities.UserNotification
import java.util.UUID

class UserNotificationRepo(private val db: AppDatabase) : SafeAPIRequest() {
    suspend fun pullNotifications(ctx: Context): List<UserNotification> {
        val response = apiRequest(ctx, { API.getClient().fetch_notification() })
        response?.let { db.userNotificationDao().upsertAll(it.notifications) }
        return db.userNotificationDao().pullNotifications()
    }

    suspend fun createNotification(title: String, text: String) {
        val currentUser = db.userDao().getCurrentUser() ?: return
        val notification = UserNotification(
            user_id = currentUser.user_id,
            title = title,
            notification_text = text,
            status = NOTI_Status.UNREAD,
            notification_id = UUID.randomUUID(),
            created_at = java.util.Date()
        )
        db.userNotificationDao().upsertAll(listOf(notification))
    }
}