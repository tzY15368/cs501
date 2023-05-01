package com.cs501.cs501app.buotg.view.homeScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import com.cs501.cs501app.buotg.view.dayNightTheme.EventTrackerTheme
import android.content.Intent
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
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LifecycleOwner
import com.cs501.cs501app.R
import com.cs501.cs501app.buotg.database.entities.Event
import com.cs501.cs501app.buotg.database.entities.USER_LATITUDE_VAL_TO
import com.cs501.cs501app.buotg.database.entities.USER_LONGITUDE_VAL_TO
import com.cs501.cs501app.buotg.database.entities.User
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
import kotlinx.coroutines.runBlocking
import java.util.*

class HomeActivity : AppCompatActivity() {
    val eventRepo = AppRepository.get().eventRepo()
    val userRepo = AppRepository.get().userRepo()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
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
                ) {
                        paddingVal->
                    EventTracker()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun EventTracker(
        modifier: Modifier = Modifier
    ) {

        val sheetState = rememberModalBottomSheetState(
            ModalBottomSheetValue.Hidden
        )
        val scope = rememberCoroutineScope()
        var currentUser by remember { mutableStateOf<User?>(null) }
        var events by remember { mutableStateOf(listOf<Event>()) }
        var currentEvent by remember { mutableStateOf<Event?>(null) }
        val context = LocalContext.current

        suspend fun setCURRENT_UID(): UUID {
            return AppRepository.get().userRepo().getCurrentUser()!!.user_id
        }

        suspend fun reloadEvents() {
            currentUser = userRepo.getCurrentUser()
            val resp = eventRepo.listEvents(context)
            if (resp != null) {
                events = resp.events
            }
            val CURRENT_UID: UUID by lazy {
                runBlocking { setCURRENT_UID() }
            }
            Log.d("CURRENT_UID", events.toString())
            currentEvent = Event(event_id = UUID.randomUUID(), event_name = "Empty Event", latitude = USER_LATITUDE_VAL_TO.toFloat(), longitude = USER_LONGITUDE_VAL_TO.toFloat(), start_time = Date(), end_time = Date(),
                repeat_mode = 0, priority = 1, desc = "Empty Event description",
                notify_time = 0)
        }

        LaunchedEffect(true) {
            reloadEvents()
        }


        currentEvent?.let {
            EventBottomSheet(
                event = it,
                modifier = modifier,
                onCancel = {
                    scope.launch {
                        sheetState.hide()
                    }
                },
                onSubmit = { updatedEvent ->
                    scope.launch {
                        eventRepo.upsertEvent(context, updatedEvent)
                        sheetState.hide()
                        Log.d("EventTracker", "Event saved: $updatedEvent")
                        reloadEvents()
                    }
                },
                sheetState = sheetState
            )
            {
                Scaffold(
                    floatingActionButton = {
                        EventTrackerFAB(
                            onClick = {
                                scope.launch {
                                    reloadEvents()
                                    sheetState.show()
                                }
                            }
                        )
                    }
                ) { contentPadding ->
                    Column(Modifier.padding(contentPadding)) {
                        EventTrackerList(
                            events = events,
                            onDelete = { event ->
                                scope.launch {
                                    eventRepo.deleteEvent(context, event)
                                    reloadEvents()
                                }
                            },
                            onUpdate = { event ->
                                scope.launch {
                                    eventRepo.upsertEvent(context, event)
                                    sheetState.show()
                                    reloadEvents()
                                }
                            },
                            onShowSharedEvents = { event ->
                                val intent = Intent(context, SharedEventActivity::class.java)
                                Log.d("EventTracker0", intent.toString())
                                Log.d("EventTracker", "Event ID: ${event.event_id}")
                                intent.putExtra("eventId", event.event_id.toString())
                                context.startActivity(intent)
                            },
                        )
                    }
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
                title = "Home",
                contentDescription = "Goto home",
                icon = Icons.Default.Home,
                bindClass = HomeActivity::class.java
            ),
            MenuItem(
                title = "Setup",
                contentDescription = "Goto setup",
                icon = Icons.Default.AccountBox,
                bindClass = SetupActivity::class.java
            ),
            MenuItem(
                title = "Study Group",
                contentDescription = "Goto Study Group",
                icon = Icons.Default.Person,
                bindClass = StudyGroupActivity::class.java
            ),
            MenuItem(
                title = "Map View",
                contentDescription = "Goto Map View",
                icon = Icons.Default.LocationOn,
                bindClass = MapViewActivity::class.java
            ),
            MenuItem(
                title = "Invite",
                contentDescription = "Goto My Invite",
                icon = Icons.Default.Info,
                bindClass = InviteActivity::class.java
            ),
            MenuItem(
                title = "Chat Room",
                contentDescription = "Goto Chat Room (Groups)",
                icon = Icons.Default.Email,
                bindClass = ChatRoomActivity::class.java
            ),
            MenuItem(
                title = "Setting",
                contentDescription = "Goto setting",
                icon = Icons.Default.Settings,
                bindClass = SettingActivity::class.java
            ),
        ),
        onItemClick = {
            println("Clicked on ${it.title}")
            val intent = Intent(ctx, it.bindClass)
            startActivity(ctx, intent, null)
        }
    )
}