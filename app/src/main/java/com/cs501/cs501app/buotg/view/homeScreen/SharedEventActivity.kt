package com.cs501.cs501app.buotg.view.homeScreen

import android.os.Bundle
import android.util.Log
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
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cs501.cs501app.buotg.database.entities.SharedEvent

import androidx.compose.ui.window.Dialog
import com.cs501.cs501app.R
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
            contentDescription = stringResource(id = R.string.take_attendence),
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
            contentDescription = stringResource(id = R.string.import_members),
            modifier = Modifier.size(20.dp)
        )
    }
}

class SharedEventActivity : AppCompatActivity() {

    val userRepo = AppRepository.get().userRepo()
    val sharedEventRepo = AppRepository.get().sharedEventRepo()
    val sharedEventParticipanceRepo = AppRepository.get().sharedEventParticipanceRepo()
    var eventId : UUID? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        eventId = intent.getStringExtra("eventId")?.let { UUID.fromString(it) }
        Log.d("SharedEventActivity", "eventId: $eventId")
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
        val startDestination = "event/sharedEvents"
        Log.d("SharedEventActivity", "destination: $startDestination")
        NavHost(navController = navController, startDestination = startDestination) {
            composable("event/sharedEvents") { backStackEntry ->
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
        var newSharedEventName by remember { mutableStateOf("") }
        var creatingSharedEvent by remember { mutableStateOf(false) }
        var newSharedEventDesc by remember { mutableStateOf("") }
        var SharedEvents by remember { mutableStateOf(listOf<SharedEvent>()) }
        var currentUser by remember { mutableStateOf<User?>(null) }

        suspend fun reloadSharedEvents() {
            currentUser = userRepo.getCurrentUser()
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
                //@TODO Is this expression fluent?
                val creator = stringResource(id = R.string.creator)
                val by = if (createdbyUser != null) " $creator: " + createdbyUser!!.full_name else ""
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
                        Text(text = stringResource(id = R.string.import_members_2))
                    }
                    val group_id = stringResource(id = R.string.group_id)
                    Text(text = "$group_id:", fontSize = 20.sp)
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
                        Text(text = stringResource(id = R.string.import_members_2))
                        TextField(
                            value = groupId.toString(),
                            onValueChange = { groupId = it.toInt() },
                            label = { Text(text = stringResource(id = R.string.group_id)) },
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
                Text(text = stringResource(id = R.string.no_shared_event_found))
            }
            Column {
                for (SharedEvent in SharedEvents) {
                    SharedEventView(SharedEvent = SharedEvent)
                }
            }
        }
        val scaffoldState = rememberScaffoldState()
        val scope = rememberCoroutineScope()
        Scaffold(
            topBar = {
                GenericTopAppBar(title = stringResource(id = R.string.shared_events),   onNavigationIconClick = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                },
                finished = {finish()})
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
                            Text(text = stringResource(id = R.string.create_shared_event))
                        }
                    }
                }


            }
        }

        //create SharedEvent
        if (creatingSharedEvent) {
            Log.d("SharedEventActivity_create?", "BEGIN")
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
                    Text(text = stringResource(id = R.string.create_a_shared_event))
                    TextField(
                        value = newSharedEventName,
                        onValueChange = { newSharedEventName = it },
                        label = { Text(text = stringResource(id = R.string.shared_event_name)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )
                    TextField(
                        value = newSharedEventDesc,
                        onValueChange = { newSharedEventDesc = it },
                        label = { Text(text = stringResource(id = R.string.shared_event_desc)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )
                    Button(onClick = {
                        Log.d("CLICKED", eventId.toString())
                        Log.d("CLICKED", currentUser.toString())
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
                            Log.d("CLICKED", sharedEvent.toString())
                            if (sharedEvent != null) {
                                Log.d("createSharedEvent", sharedEvent.toString())
                                sharedEventRepo.updateSharedEvent(sharedEvent, ctx)
                            }
                            reloadSharedEvents()
                        }
                        creatingSharedEvent = false
                    }) {
                        Text(text = stringResource(id = R.string.create))
                    }
                }
            }
        }


    }

}




