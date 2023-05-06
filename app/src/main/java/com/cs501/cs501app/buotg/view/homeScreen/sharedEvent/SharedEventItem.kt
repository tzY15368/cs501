package com.cs501.cs501app.buotg.view.homeScreen.sharedEvent

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
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
import com.cs501.cs501app.buotg.connection.SharedEventListItem
import com.cs501.cs501app.buotg.database.entities.*
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import com.cs501.cs501app.buotg.database.repositories.UserRepository

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SharedEventItem(
    eventData: SharedEventListItem,
    currentUser: MutableState<User?>,
    userRepo: UserRepository,
    reloadSharedEvents: suspend () -> Unit,
    onNavigateToSharedEventDetails: (eventId: Int) -> Unit,
    event: Event?
) {
    var createdbyUser by remember { mutableStateOf<User?>(null) }
    //var takingAttendance by remember { mutableStateOf(false) }
    var importingGroupMembers by remember { mutableStateOf(false) }
    var viewingParticipance by remember { mutableStateOf(false) }
    val sharedEventParticipanceRepo = AppRepository.get().sharedEventParticipanceRepo()
    val ctx = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(true) {
        currentUser.value = userRepo.getCurrentUser()
        userRepo.fetchUser(ctx, eventData.shared_event.owner_id)?.let {
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
            text = eventData.shared_event.shared_event_id.toString() + by + "\n" + eventData.shared_event.created_at,
            fontSize = 30.sp,
            modifier = Modifier.clickable {
                onNavigateToSharedEventDetails(eventData.shared_event.shared_event_id)
            })
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (currentUser.value != null) {

                takeAttendanceBtn(
                    eventData = eventData,
                    userId = currentUser.value!!.user_id,
                    eventLocation = event?.toLocation(),
                    reload = { reloadSharedEvents() }
                )
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
            importUsersView(
                sharedEventId = eventData.shared_event.shared_event_id,
                reloadSharedEvents = { reloadSharedEvents() },
            )
        }
    }
    if (viewingParticipance) {
        Log.d("view participants start", "view participants start")
        Dialog(onDismissRequest = { viewingParticipance = false }) {
            viewParticipantListBtn(sharedEventId = eventData.shared_event.shared_event_id)
        }
    }
}
