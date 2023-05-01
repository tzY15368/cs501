package com.cs501.cs501app.buotg.database.dao

import androidx.room.*
import com.cs501.cs501app.buotg.database.entities.CURRENT_USER_KEY
import com.cs501.cs501app.buotg.database.entities.UserNotification
import java.util.UUID

@Dao
interface UserNotificationDao {
    /*
    notifications = UserNotification.query.filter_by(user_id=user_id, status='UNREAD').all()
    for notification in notifications:
        notification.status = 'READ'
     */
    @Query("SELECT * from user_notification where user_id=(SELECT value FROM kv_entry WHERE key = (:userKey)) and status='UNREAD'")
    suspend fun getUserNotifications(USER_ID_KEY:String= CURRENT_USER_KEY): List<UserNotification>

    @Query("UPDATE user_notification SET status='READ' where notification_id in (:notificationIds)")
    suspend fun markNotificationsAsRead(notificationIds:List<UUID>)

    @Transaction
    suspend fun pullNotifications(): List<UserNotification>{
        val notifications = getUserNotifications()
        val notificationIds = notifications.map { it.notification_id }
        markNotificationsAsRead(notificationIds)
        return notifications
    }

    @Upsert
    suspend fun upsertAll(userNotifications: List<UserNotification>)
}