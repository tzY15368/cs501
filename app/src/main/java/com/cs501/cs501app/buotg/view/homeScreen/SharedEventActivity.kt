package com.cs501.cs501app.buotg.view.homeScreen

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import kotlin.random.Random
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.runtime.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cs501.cs501app.buotg.database.entities.SharedEvent

import androidx.compose.ui.window.Dialog
import com.cs501.cs501app.buotg.database.entities.SharedEventParticipance
import com.cs501.cs501app.buotg.database.entities.Status
import com.cs501.cs501app.buotg.database.entities.User
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import com.cs501.cs501app.buotg.database.repositories.SharedEventRepo
import com.cs501.cs501app.utils.GenericTopAppBar
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun takeAttendanceBtn(sharedEventId: Int, userId: UUID, callback: suspend () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val sharedEventParticipanceRepo = AppRepository.get().sharedEventParticipanceRepo()
    val ctx = LocalContext.current
    OutlinedButton(modifier = Modifier.size(30.dp),
        onClick = {
            val participance = SharedEventParticipance(shared_event_id = sharedEventId, user_id = userId, status = Status.SUCCESS)
            coroutineScope.launch {
                sharedEventParticipanceRepo.updateParticipance(participance)
                callback()
            }
        }) {

        Icon(
            Icons.Outlined.Add,
            contentDescription = "Take Attendance",
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun importUsersBtn(sharedEventId: Int, groupId: Int, callback: suspend () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val sharedEventParticipanceRepo = AppRepository.get().sharedEventParticipanceRepo()
    val groupMemberRepo = AppRepository.get().groupMemberDao()
    val ctx = LocalContext.current
    OutlinedButton(modifier = Modifier.size(30.dp),
        onClick = {
            coroutineScope.launch {
                var groupMembers = groupMemberRepo.getGroupMembersById(groupId)
                for(gm in groupMembers) {
                    val participance = SharedEventParticipance(shared_event_id = sharedEventId, user_id = gm.user_id, status = Status.FAIL)
                    sharedEventParticipanceRepo.updateParticipance(participance)
                }
                sharedEventParticipanceRepo
            }

        }) {
        Icon(
            Icons.Outlined.Add,
            contentDescription = "Import Study Group Members",
            modifier = Modifier.size(20.dp)
        )
    }
}

class SharedEventActivity : AppCompatActivity() {

    val userRepo = AppRepository.get().userRepo()
    val sharedEventRepo = AppRepository.get().sharedEventRepo()
    val sharedEventParticipanceRepo = AppRepository.get().sharedEventParticipanceRepo()
    val eventId = intent.getStringExtra("eventId")?.let { UUID.fromString(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                SharedEventNavHost()
            }
        }
    }

    @Composable
    fun SharedEventNavHost(

    ) {
        val navController = rememberNavController()
        val startDestination = "event/{eventId}"
        NavHost(navController = navController, startDestination = startDestination) {
            composable("event/{eventId}/sharedEvents") { backStackEntry ->
                println(backStackEntry.arguments)
                ShowView(onNavigateToSharedEventDetails = {
                    navController.navigate("event/{eventId}/sharedEvent/$it")
                })
            }
            composable("sharedEvents/{sharedEventId}") { backStackEntry ->
                println(backStackEntry.arguments)
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
        var newSharedEventName by remember { mutableStateOf("") }
        var creatingSharedEvent by remember { mutableStateOf(false) }
        var newSharedEventDesc by remember { mutableStateOf("") }
        var SharedEvents by remember { mutableStateOf(listOf<SharedEvent>()) }
        var currentUser by remember { mutableStateOf<User?>(null) }

        suspend fun reloadSharedEvents() {

            val resp = eventId?.let { sharedEventRepo.getAllSharedEventByEventId(it,ctx) }
            if (resp != null) {
                SharedEvents = resp.sharedEvents
            }
        }

        @Composable
        fun SharedEventView(SharedEvent: SharedEvent) {
            var createdbyUser by remember { mutableStateOf<User?>(null) }
            var importingGroupMembers by remember { mutableStateOf(false) }
            var groupId by remember { mutableStateOf(0) }
            LaunchedEffect(true) {
                currentUser = userRepo.getCurrentUser()
                userRepo.fetchUser(ctx, SharedEvent.owner_id)?.let {
                    createdbyUser = it
                }

            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                val by = if (createdbyUser != null) " by " + createdbyUser!!.full_name else ""
                Text(
                    text = SharedEvent.shared_event_id.toString() + by,
                    fontSize = 30.sp,
                    modifier = Modifier.clickable {
                        onNavigateToSharedEventDetails(SharedEvent.shared_event_id)
                    })
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = SharedEvent.shared_event_id.toString(), fontSize = 15.sp)
                    currentUser?.let { takeAttendanceBtn(sharedEventId = SharedEvent.shared_event_id, userId = it.user_id, callback = { reloadSharedEvents() }) }
                    Button(onClick = { importingGroupMembers = true }) {
                        Text(text = "Import Group Members")
                    }
                    Text(text = "Group id:", fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.height(10.dp))
                Divider()
            }


            //import group members
            if (importingGroupMembers) {
                Dialog(onDismissRequest = { importingGroupMembers = false }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(text = "Import Group Members")
                        TextField(
                            value = groupId.toString(),
                            onValueChange = { groupId = it.toInt() },
                            label = { Text(text = "Group Id") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                        )
                        importUsersBtn(sharedEventId =SharedEvent.shared_event_id, groupId = groupId, callback = { reloadSharedEvents() })
                    }
                }
            }

        }

        @Composable
        fun SharedEventList() {
            val ctx = LocalContext.current
            LaunchedEffect(true) {
                reloadSharedEvents()
            }
            if(SharedEvents.isEmpty()) {
                Text(text = "No SharedEvents found")
            }
            Column {
                for (SharedEvent in SharedEvents) {
                    SharedEventView(SharedEvent = SharedEvent)
                }
            }
        }

        Scaffold(
            topBar = {
                GenericTopAppBar(title = "Shared Events")
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
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
                        Button(onClick = { creatingSharedEvent = true }) {
                            Text(text = "Create SharedEvent")
                        }
                    }
                }


            }
        }


        //create SharedEvent
        if (creatingSharedEvent) {
            Dialog(onDismissRequest = { creatingSharedEvent = false }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(text = "Create a Shared Event")
                    TextField(
                        value = newSharedEventName,
                        onValueChange = { newSharedEventName = it },
                        label = { Text(text = "Shared Event name") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )
                    TextField(
                        value = newSharedEventDesc,
                        onValueChange = { newSharedEventDesc = it },
                        label = { Text(text = "Shared Event description") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )
                    Button(onClick = {
                        coroutineScope.launch {
                            val sharedEvent = eventId?.let {
                                currentUser?.let { it1 ->
                                    SharedEvent(
                                        event_id = it,
                                        shared_event_id = Random.nextInt(Int.MIN_VALUE, Int.MAX_VALUE),
                                        owner_id = it1.user_id,
                                        created_at = Date(),
                                        checkin_time = null
                                    )
                                }
                            }
                            if (sharedEvent != null) {
                                sharedEventRepo.updateSharedEvent(sharedEvent, ctx)
                            }
                            reloadSharedEvents()
                        }
                        creatingSharedEvent = false
                    }) {
                        Text(text = "Create")
                    }
                }
            }
        }


    }

}




