package com.cs501.cs501app.buotg.database.entities

import java.util.Date
import java.util.UUID

/*
    notification_id = db.Column(CHAR(36, charset='utf8mb4'), primary_key=True, default=str(uuid.uuid4()))
    user_id = db.Column(CHAR(36, charset='utf8mb4'))
    title = db.Column(db.String(255))
    notification_text = db.Column(db.Text)
    created_at = db.Column(db.DateTime, default=db.func.current_time())
    status = db.Column(db.Enum('UNREAD', 'READ'))
 */

enum class NOTI_Status {
    UNREAD,
    READ
}

data class UserNotification(
    val notification_id: UUID,
    val user_id: UUID,
    val title: String,
    val notification_text: String,
    val created_at: Date,
    val status: NOTI_Status
)