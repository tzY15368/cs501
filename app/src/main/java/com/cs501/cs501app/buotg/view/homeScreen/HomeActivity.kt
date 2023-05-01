package com.cs501.cs501app.buotg.view.homeScreen

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import com.cs501.cs501app.buotg.view.dayNightTheme.EventTrackerTheme
import android.content.Intent
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import com.cs501.cs501app.buotg.view.bottomsheet.EventBottomSheet
import android.util.Log
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.cs501.cs501app.R
import com.cs501.cs501app.buotg.database.entities.Event
import com.cs501.cs501app.buotg.database.entities.USER_LATITUDE_VAL_TO
import com.cs501.cs501app.buotg.database.entities.USER_LONGITUDE_VAL_TO
import com.cs501.cs501app.buotg.database.entities.User
import com.cs501.cs501app.buotg.view.common.CHANNEL_DESCRIPTION
import com.cs501.cs501app.buotg.view.common.CHANNEL_NAME
import com.cs501.cs501app.buotg.view.common.DEFAULT_CHANNEL_ID
import com.cs501.cs501app.buotg.view.thirdParty.chatRoom.ChatRoomActivity
import com.cs501.cs501app.buotg.view.user_group.StudyGroupActivity
import com.cs501.cs501app.buotg.view.user_invite.InviteActivity
import com.cs501.cs501app.buotg.view.user_map.MapViewActivity
import com.cs501.cs501app.buotg.view.user_setting.SettingActivity
import com.cs501.cs501app.buotg.view.user_setup.SetupActivity
import com.cs501.cs501app.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import java.util.*
import java.util.concurrent.TimeUnit

val POLL_STATE_KEY = "POLL_STATE_KEY"
val WORK_NAME = "BUOTG-POLLWORKER"

class HomeActivity : AppCompatActivity() {
    private val coroutineScope = CoroutineScope(this.lifecycle.coroutineScope.coroutineContext)
    override fun onStart() {
        super.onStart()
        createNotificationChannel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        coroutineScope.launch {
            setupBackgroundWork()
        }
        setContent {
            EventTrackerTheme {
                val scaffoldState = rememberScaffoldState()
                val scope = rememberCoroutineScope()
                val appBarName = stringResource(R.string.event_app_name)
                EventTracker()
                androidx.compose.material.Scaffold(
                    scaffoldState = scaffoldState,
                    topBar = {
                        GenericTopAppBar(
                            title = appBarName,
                            onNavigationIconClick = {
                                scope.launch {
                                    scaffoldState.drawerState.open()
                                }
                            },
                            hasNavMenu = true
                        )
                    },
                    drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
                    drawerContent = {
                        NavDrawer(reload={setupBackgroundWork()})
                    }
                ) { paddingVal ->
                    EventTracker()
                }
            }
        }

        if (ActivityCompat.checkSelfPermission(
                this,
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
            requestPermissions(
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1
            )
            TAlert.fail(this, "No permission to post notification")
            return
        }
    }
    override fun onResume() {
        super.onResume()
    }

    suspend fun setupBackgroundWork(){
        val kvDao = AppRepository.get().kvDao()
        kvDao.get(POLL_STATE_KEY)?.let {
            if(it.value == "true"){
                val periodicRequest = PeriodicWorkRequestBuilder<PollWorker>(15, TimeUnit.MINUTES)
                    .build()
                WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                    WORK_NAME,
                    ExistingPeriodicWorkPolicy.KEEP,
                    periodicRequest
                )
                Log.d("HomeActivity", "Polling is enabled")
            }else{
                WorkManager.getInstance(this).cancelUniqueWork(WORK_NAME)
                Log.d("HomeActivity", "Polling is disabled")
            }
        }
    }

    fun createNotificationChannel(){
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(DEFAULT_CHANNEL_ID, CHANNEL_NAME, importance).apply {
            description = CHANNEL_DESCRIPTION
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}