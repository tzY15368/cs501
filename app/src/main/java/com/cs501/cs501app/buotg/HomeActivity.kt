package com.cs501.cs501app.buotg

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.startActivity
import com.cs501.cs501app.MainActivity
import com.cs501.cs501app.buotg.view.dayNightTheme.EventTrackerTheme
import com.cs501.cs501app.buotg.view.homeScreen.EventTracker
import com.cs501.cs501app.buotg.view.homeScreen.SharedEventActivity
import com.cs501.cs501app.buotg.view.thirdParty.chatRoom.ChatRoomActivity
import com.cs501.cs501app.buotg.view.user_group.StudyGroupActivity
import com.cs501.cs501app.buotg.view.user_invite.InviteActivity
import com.cs501.cs501app.buotg.view.user_map.MapViewActivity
import com.cs501.cs501app.buotg.view.user_setting.SettingActivity
import com.cs501.cs501app.buotg.view.user_setup.SetupActivity
import com.cs501.cs501app.utils.DrawerBody
import com.cs501.cs501app.utils.DrawerHeader
import com.cs501.cs501app.utils.GenericTopAppBar
import com.cs501.cs501app.utils.MenuItem
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter",
        "UnusedMaterial3ScaffoldPaddingParameter"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            EventTrackerTheme {
                val scaffoldState = rememberScaffoldState()
                val scope = rememberCoroutineScope()
                androidx.compose.material.Scaffold(
                    scaffoldState = scaffoldState,
                    topBar = {
                        GenericTopAppBar(
                            title = "Event Tracker",
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
                ) {
                    paddingVal->
                    EventTracker()
                }
            }
        }
    }
}

@Composable
fun NavDrawer(){
    val ctx = LocalContext.current
    DrawerHeader()
    DrawerBody(
        items = listOf(
            MenuItem(
                id = 0,
                title = "Home",
                contentDescription = "Goto home",
                icon = Icons.Default.Home,
                bindClass = HomeActivity::class.java
            ),
            MenuItem(
                id = 1,
                title = "Setting",
                contentDescription = "Goto setting",
                icon = Icons.Default.Settings,
                bindClass = SettingActivity::class.java
            ),
            MenuItem(
                id = 2,
                title = "Setup",
                contentDescription = "Goto setup",
                icon = Icons.Default.Info,
                bindClass = SetupActivity::class.java
            ),
            MenuItem(
                id = 3,
                title = "Study Group",
                contentDescription = "Goto Study Group",
                icon = Icons.Default.Info,
                bindClass = StudyGroupActivity::class.java
            ),
            MenuItem(
                id = 4,
                title = "Map View",
                contentDescription = "Goto Map View",
                icon = Icons.Default.Info,
                bindClass = MapViewActivity::class.java
            ),
            MenuItem(
                id = 5,
                title = "Invite",
                contentDescription = "Goto My Invite",
                icon = Icons.Default.Info,
                bindClass = InviteActivity::class.java
            ),
            MenuItem(
                id = 6,
                title = "Chat Room",
                contentDescription = "Goto Chat Room (Groups)",
                icon = Icons.Default.Info,
                bindClass = ChatRoomActivity::class.java
            ),
        ),
        onItemClick = {
            println("Clicked on ${it.title}")
            val intent = Intent(ctx, it.bindClass)
            startActivity(ctx, intent, null)
        }
    )
}

