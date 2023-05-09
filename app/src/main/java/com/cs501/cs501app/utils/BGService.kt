package com.cs501.cs501app.utils

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.cs501.cs501app.R
import com.cs501.cs501app.buotg.database.entities.KVEntry
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

val LOCATION_NOTIFICATION_ID = 3
val USER_NOTIFICATION_ID = 4
val BGSVC_STATE_KEY = "BGSVC_STATE_KEY"

suspend fun getBGserviceState():Boolean{
    val kvDao = AppRepository.get().kvDao()

    var bgSvcState:Boolean = false
    kvDao.get(BGSVC_STATE_KEY)?.let {
        bgSvcState = it.value == "true"
    }
    Log.d("setupBGService", "bgSvcState: $bgSvcState")
    return bgSvcState
}

suspend fun setBGServiceState(ctx: Context, state:Boolean){
    val kvDao = AppRepository.get().kvDao()
    val value = if(state) "true" else "false"
    kvDao.put(KVEntry(BGSVC_STATE_KEY, value))
    setupBGService(ctx)
}
suspend fun setupBGService(ctx:Context){
    val state = getBGserviceState()

    val _action = if(state){
        BGService.ACTION_START}else{
        BGService.ACTION_STOP}
    val intent = Intent(ctx, BGService::class.java).apply {
        action = _action
    }
    ctx.startService(intent)
}

class BGService: Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: DefaultLocationClient
    private val taskScheduler: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
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

        sendNotification(this, "Tracking location...", "Location: null", LOCATION_NOTIFICATION_ID,true)

        locationClient
            .getLocationUpdates(10000L)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                val lat = location.latitude.toString().takeLast(3)
                val long = location.longitude.toString().takeLast(3)

                val updatedText = "Location: ($lat, $long)"

                sendNotification(this, "Tracking location...", updatedText, LOCATION_NOTIFICATION_ID,true)
            }
            .launchIn(serviceScope)
        taskScheduler.scheduleAtFixedRate({
            Log.d("BGService", "taskScheduler every 10 seconds")
            val notiRepo = AppRepository.get().notificationRepo()
            val coroutineScope = CoroutineScope(Dispatchers.IO)
            val that = this;
            coroutineScope.launch {
                val notis = notiRepo.pullNotifications(that)
                for(noti in notis) {
                    Log.d("BGService", "noti: ${noti}")
                    sendNotification(that, noti.title, noti.notification_text, USER_NOTIFICATION_ID,false)
                }
            }
        }, 0, 2, java.util.concurrent.TimeUnit.SECONDS)
    }

    private fun stop() {
        stopForeground(true)
        taskScheduler.shutdown()
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