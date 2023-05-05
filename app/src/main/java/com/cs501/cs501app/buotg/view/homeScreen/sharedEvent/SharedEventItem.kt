package com.cs501.cs501app.buotg.view.homeScreen.sharedEvent

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.cs501.cs501app.R
import com.cs501.cs501app.buotg.database.entities.*
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import com.cs501.cs501app.buotg.database.repositories.UserRepository
import com.cs501.cs501app.buotg.view.user_map.getCurrentLocation
import com.cs501.cs501app.utils.TAlert
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SharedEventView(
    SharedEvent: SharedEvent,
    currentUser: MutableState<User?>,
    userRepo: UserRepository,
    reloadSharedEvents: suspend () -> Unit,
    onNavigateToSharedEventDetails: (eventId:Int) -> Unit,
    event: Event?
) {
    var createdbyUser by remember { mutableStateOf<User?>(null) }
    var takingAttendance by remember { mutableStateOf(false) }
    var importingGroupMembers by remember { mutableStateOf(false) }
    var viewingParticipance by remember { mutableStateOf(false) }
    val sharedEventParticipanceRepo = AppRepository.get().sharedEventParticipanceRepo()
    val ctx = LocalContext.current
    var groupId by remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(true) {
        currentUser.value = userRepo.getCurrentUser()
        userRepo.fetchUser(ctx, SharedEvent.owner_id)?.let {
            createdbyUser = it
        }

    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        val creator = stringResource(id = R.string.creator)
        val by =
            if (createdbyUser != null) " $creator: " + createdbyUser!!.full_name else ""
        var hasTaken = remember { mutableStateOf(false) }
        Text(
            text = SharedEvent.shared_event_id.toString() + by + "\n" + SharedEvent.created_at,
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

            Button(
                onClick = {
                    Log.d("Try_CLICKED_ATTENDANCE", SharedEvent.shared_event_id.toString())
                    var eventLocation = event?.let { it.toLocation() }
                    getCurrentLocation(ctx) { userLocation ->
                        if (userLocation != null) {
                            if (userLocation?.let { userLocation.distanceTo(it) }!! <= CHECKIN_DISTANCE) {
                                Log.d(
                                    "CLICKED_ATTENDANCE",
                                    SharedEvent.shared_event_id.toString()
                                )
                                val prev_participance = currentUser.value?.let {
                                    SharedEventParticipance(
                                        shared_event_id = SharedEvent.shared_event_id,
                                        user_id = it.user_id,
                                        status = Status.FAIL
                                    )
                                }
                                val participance = currentUser.value?.let {
                                    SharedEventParticipance(
                                        shared_event_id = SharedEvent.shared_event_id,
                                        user_id = it.user_id,
                                        status = Status.SUCCESS
                                    )
                                }
                                coroutineScope.launch {
                                    if (prev_participance != null) {
                                        sharedEventParticipanceRepo.deleteParticipance(
                                            prev_participance,
                                            ctx
                                        )
                                    }
                                    if (participance != null) {
                                        sharedEventParticipanceRepo.updateParticipance(
                                            participance,
                                            ctx
                                        )
                                    }

                                }

                                hasTaken.value = true
                            } else {
                                Log.d(
                                    "CANNOT_TAKE_ATTEND",
                                    SharedEvent.shared_event_id.toString()
                                )
                                TAlert.fail(ctx, "Away from valid scope!")
                            }
                        }
                    }
                }, enabled = !hasTaken.value
            ) {
                Text(text = stringResource(id = R.string.take_attendence))
            }

            Button(onClick = {
                importingGroupMembers = true
            }) {
                Text(text = stringResource(id = R.string.import_members_2))
            }


        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = {
                viewingParticipance = true
            }) {
                Text(text = stringResource(id = R.string.view_participant))
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Divider()
    }


    //import group members
    if (importingGroupMembers) {
        TranslucentDialog(onDismissRequest = { importingGroupMembers = false }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(16.dp),
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
                importUsersBtn(
                    sharedEventId = SharedEvent.shared_event_id,
                    groupId = groupId,
                    callback = { reloadSharedEvents() })
            }
        }
    }
    if (takingAttendance) {
        Log.d("take attendance start", "take attendance start")
        Log.d("event", event.toString())
        event?.let { it.toLocation().toString() }?.let { Log.d("LOCATION", it) }
        Dialog(onDismissRequest = { takingAttendance = false }) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),

                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(text = stringResource(id = R.string.take_attendence))
                event?.let {
                    it.toLocation()
                }?.let {
                    currentUser.value?.let { it1 ->
                        takeAttendanceBtn(
                            sharedEventId = SharedEvent.shared_event_id,
                            eventLocation = it,
                            userId = it1.user_id,
                            callback = { reloadSharedEvents() })
                    }
                }
            }
        }
    }
    if (viewingParticipance) {
        Log.d("view participants start", "view participants start")
        Dialog(onDismissRequest = { viewingParticipance = false }) {
            viewParticipantListBtn(sharedEventId = SharedEvent.shared_event_id)
        }
    }
}
