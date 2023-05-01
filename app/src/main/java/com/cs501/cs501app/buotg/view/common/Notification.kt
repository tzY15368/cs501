package com.cs501.cs501app.buotg.view.common

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.cs501.cs501app.buotg.view.homeScreen.HomeActivity
import com.cs501.cs501app.utils.TAlert

val DEFAULT_CHANNEL_ID = "default_channel_id"

val CHANNEL_NAME = "buotg-noti-channel"
val CHANNEL_DESCRIPTION = "buotg's notification channel'"




fun testNoti(ctx:Context, title:String, content:String){

    val tapIntent = Intent(ctx, HomeActivity::class.java).apply{
        flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    val pendingIntent = PendingIntent.getActivity(ctx, 0, tapIntent, PendingIntent.FLAG_IMMUTABLE)

    var builder = NotificationCompat.Builder(ctx, DEFAULT_CHANNEL_ID)
        .setSmallIcon(androidx.core.R.drawable.notification_icon_background)
        .setContentTitle(title)
        .setContentText(content)
        .setContentIntent(pendingIntent)
        .setDefaults(NotificationCompat.DEFAULT_ALL)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
    with(NotificationManagerCompat.from(ctx)){
        if (ActivityCompat.checkSelfPermission(
                ctx,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            TAlert.fail(ctx, "No permission to post notification")
            return
        }
        notify(0, builder.build())
    }
}