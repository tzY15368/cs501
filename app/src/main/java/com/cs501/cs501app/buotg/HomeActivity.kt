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
    @OptIn(ExperimentalMaterial3Api::class)
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
                            }
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



fun onClickActivity(ctx: Context, activityIdx: Int){
    val activity = activities[activityIdx]
    val intent = Intent(ctx, activity.java)
    startActivity(ctx, intent, null)
}
val activities = listOf(
    HomeActivity::class,
    SetupActivity::class,
    SettingActivity::class,
    StudyGroupActivity::class,
    SharedEventActivity::class,
    MapViewActivity::class,
    InviteActivity::class,
    ChatRoomActivity::class,
    MainActivity::class
)
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
                icon = Icons.Default.Home
            ),
            MenuItem(
                id = 1,
                title = "Setting",
                contentDescription = "Goto setting",
                icon = Icons.Default.Settings
            ),
            MenuItem(
                id = 2,
                title = "Setup",
                contentDescription = "Goto setup",
                icon = Icons.Default.Info
            ),
            MenuItem(
                id = 3,
                title = "Study Group",
                contentDescription = "Goto Study Group",
                icon = Icons.Default.Info
            ),
            MenuItem(
                id = 4,
                title = "SharedEvent",
                contentDescription = "Goto Shared Event",
                icon = Icons.Default.Info
            ),
            MenuItem(
                id = 5,
                title = "Map View",
                contentDescription = "Goto Map View",
                icon = Icons.Default.Info
            ),
            MenuItem(
                id = 6,
                title = "Invite",
                contentDescription = "Goto Invite",
                icon = Icons.Default.Info
            ),
            MenuItem(
                id = 7,
                title = "Chat Room",
                contentDescription = "Goto Chat Room",
                icon = Icons.Default.Info
            ),
        ),
        onItemClick = {
            println("Clicked on ${it.title}")
            onClickActivity(ctx, activityIdx = it.id)
        }
    )
}

