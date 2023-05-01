package com.cs501.cs501app.utils

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.cs501.cs501app.R
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

val LOCATION_NOTIFICATION_ID = 3

class BGService: Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: DefaultLocationClient

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("BGService", "onStartCommand")
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
//        val notification = NotificationCompat.Builder(this, DEFAULT_CHANNEL_ID)
//            .setContentTitle("Tracking location...")
//            .setContentText("Location: null")
//            .setSmallIcon(R.drawable.ic_launcher_background)
//            .setOngoing(true)

        sendNotification(this, "Tracking location...", "Location: null", LOCATION_NOTIFICATION_ID,true)

//        val notificationManager =
//            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        locationClient
            .getLocationUpdates(10000L)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                val lat = location.latitude.toString().takeLast(3)
                val long = location.longitude.toString().takeLast(3)
//                val updatedNotification = notification.setContentText(
//                    "Location: ($lat, $long)"
//                )
                val updatedText = "Location: ($lat, $long)"
                //notificationManager.notify(1, updatedNotification.build())
                sendNotification(this, "Tracking location...", updatedText, LOCATION_NOTIFICATION_ID,true)
            }
            .launchIn(serviceScope)

        //startForeground(1, notification.build())
    }

    private fun stop() {
        stopForeground(true)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }
}