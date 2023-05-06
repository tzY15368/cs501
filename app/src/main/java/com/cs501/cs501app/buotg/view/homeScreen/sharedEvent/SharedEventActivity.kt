package com.cs501.cs501app.buotg.view.homeScreen.sharedEvent

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.cs501.cs501app.R
import com.cs501.cs501app.buotg.connection.SharedEventListItem
import com.cs501.cs501app.buotg.database.entities.*
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import com.cs501.cs501app.buotg.view.thirdParty.chatRoom.ChatApplication
import com.cs501.cs501app.utils.GenericTopAppBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Thread.sleep
import java.util.*

const val CHECKIN_DISTANCE = 1000.0

class SharedEventActivity : AppCompatActivity() {

    val userRepo = AppRepository.get().userRepo()
    val eventRepo = AppRepository.get().eventRepo()
    val sharedEventRepo = AppRepository.get().sharedEventRepo()
    val sharedEventParticipanceRepo = AppRepository.get().sharedEventParticipanceRepo()
    var eventId: UUID? = null
    var event: Event? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val chatClient = (application as ChatApplication).client
        val name = (application as ChatApplication).userName
        eventId = intent.getStringExtra("eventId")?.let { UUID.fromString(it) }
        Log.d("SharedEventActivity", "eventId: $eventId")
        setContent {
            MaterialTheme {
                SharedEventNavHost()
            }
        }
    }

    @Composable
    fun SharedEventNavHost() {
        val navController = rememberNavController()
        val startDestination = "event/shared_event"
        Log.d("SharedEventActivity", "destination: $startDestination")
        NavHost(navController = navController, startDestination = startDestination) {
            composable("event/shared_event") { backStackEntry ->
                Log.d("SharedEventActivityback1", "arguments: ${backStackEntry.arguments}")
                ShowView(onNavigateToSharedEventDetails = {
                    navController.navigate("event/sharedEvent/$it")
                })
            }
            composable("event/sharedEvent/{sharedEventId}") { backStackEntry ->
                Log.d("SharedEventActivityback2", "arguments: ${backStackEntry.arguments}")
                SharedEventDetail(Integer.parseInt(backStackEntry.arguments?.getString("sharedEventId")))
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SharedEventDetail(sharedEventId: Int) {
        var currentUser by remember { mutableStateOf<User?>(null) }
        var sharedEvent by remember { mutableStateOf<SharedEvent?>(null) }
        var sharedEventCreator by remember { mutableStateOf<SharedEvent?>(null) }

//        var sharedEvent
        val ctx = LocalContext.current

    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ShowView(onNavigateToSharedEventDetails: (Int) -> Unit) {

        val coroutineScope = rememberCoroutineScope()
        val ctx = LocalContext.current
        var creatingSharedEvent = remember { mutableStateOf(false) }
        var sharedEvents by remember { mutableStateOf(listOf<SharedEventListItem>()) }
        var event by remember { mutableStateOf<Event?>(null) }
        var currentUser = remember { mutableStateOf<User?>(null) }

        suspend fun reloadSharedEvents() {
            currentUser.value = userRepo.getCurrentUser()
            Log.d("reload event id ", eventId.toString())
            event = eventId?.let { eventRepo.getEventById(ctx, eventId = it)?.event }
            Log.d("reload", event.toString())

            val resp = eventId?.let { sharedEventRepo.getAllSharedEventByEventId(it, ctx) }
            if (resp != null) {

                println("got shared events size ${sharedEvents.size}")
                sharedEvents = resp.shared_events
            }
        }


        @Composable
        fun SharedEventList() {
            val ctx = LocalContext.current
            LaunchedEffect(true) {
                reloadSharedEvents()
            }
            if (sharedEvents.isEmpty()) {
                Text(text = stringResource(id = R.string.no_shared_event_found))
            }
            LazyColumn(
                content = {
                    items(sharedEvents.size) { idx ->
                        SharedEventItem(
                            eventData = sharedEvents[idx],
                            currentUser,
                            userRepo,
                            {reloadSharedEvents()},
                            {onNavigateToSharedEventDetails(idx)},
                            event
                        )
                    }
                }, modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
            )
        }

        val scaffoldState = rememberScaffoldState()

        val scope = rememberCoroutineScope()
        Scaffold(
            topBar = {
                GenericTopAppBar(
                    title = stringResource(id = R.string.shared_events),
                    onNavigationIconClick = {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    },
                    finished = { finish() })
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
            ) {

                SharedEventList()

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Row() {
                        Button(onClick = { creatingSharedEvent.value = true }) {
                            Text(text = stringResource(id = R.string.create_shared_event))
                        }
                    }
                }


            }
        }

        //create SharedEvent
        if (creatingSharedEvent.value) {
            Log.d("SharedEventActivity_create?", "BEGIN")
            CreateSharedEventHandler(
                creatingSharedEvent,
                { reloadSharedEvents() },
                eventId,
                currentUser,
                (application as ChatApplication)
            )
        }
    }
}


