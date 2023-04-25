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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import com.cs501.cs501app.buotg.view.bottomsheet.EventBottomSheet
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.cs501.cs501app.buotg.database.entities.Event
import com.cs501.cs501app.buotg.database.entities.User
import kotlinx.coroutines.runBlocking
import java.util.*

class HomeActivity : AppCompatActivity() {
    val eventRepo = AppRepository.get().eventRepo()
    val userRepo = AppRepository.get().userRepo()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            EventTrackerTheme {
                EventTracker()
            }
        }
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
            currentEvent = Event(event_id = UUID.randomUUID(), event_name = "Empty Event", latitude = 1234, longitude = 9876, start_time = Date(), end_time = Date(),
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
                    topBar = {
                        EventTrackerTopAppBar()
                    },
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