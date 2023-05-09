package com.cs501.cs501app.buotg.view.homeScreen

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import com.cs501.cs501app.buotg.view.dayNightTheme.EventTrackerTheme
import android.content.pm.PackageManager
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.material.*
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import com.cs501.cs501app.R
import com.cs501.cs501app.utils.CHANNEL_DESCRIPTION
import com.cs501.cs501app.utils.CHANNEL_NAME
import com.cs501.cs501app.utils.DEFAULT_CHANNEL_ID
import com.cs501.cs501app.buotg.view.homeScreen.navDrawer.NavDrawer
import com.cs501.cs501app.buotg.view.user_setting.applyCurrentLocale
import com.cs501.cs501app.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

val POLL_STATE_KEY = "POLL_STATE_KEY"
val WORK_NAME = "BUOTG-POLLWORKER"

class HomeActivity : AppCompatActivity() {
    val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onStart() {
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        coroutineScope.launch {
            applyCurrentLocale(resources.configuration, this@HomeActivity)
            setupBGService(this@HomeActivity)
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
                        NavDrawer()
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