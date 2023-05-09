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
import com.cs501.cs501app.buotg.CustomButton
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
        val createdBy = if (createdbyUser != null) "$creator: ${createdbyUser!!.full_name}" else ""
        val sharedEventId = eventData.shared_event.shared_event_id.toString()
        val sharedEventDate = eventData.shared_event.created_at

        Text(
            text = "$sharedEventId $createdBy\n$sharedEventDate",
            fontSize = 24.sp,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .clickable(onClick = { onNavigateToSharedEventDetails(eventData.shared_event.shared_event_id) })
        )

        val isStudent = currentUser.value?.user_type == UserType.student
        val isTeacher = currentUser.value?.user_type == UserType.teacher

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (isStudent) {
                takeAttendanceBtn(
                    eventData = eventData,
                    userId = currentUser.value!!.user_id,
                    eventLocation = event?.toLocation(),
                    reload = { reloadSharedEvents() },
                )
            }
            if (isTeacher) {
                CustomButton(
                    onClick = { importingGroupMembers = true },
                    modifier = Modifier.padding(4.dp),
                    text = stringResource(id = R.string.import_members_2)
                )
            }

            if (isTeacher) {
                CustomButton(
                    onClick = { viewingParticipance = true },
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .fillMaxWidth(),
                    text = stringResource(id = R.string.view_participant)
                )
            }
        }



        Spacer(modifier = Modifier.height(16.dp))
        Divider()
    }



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
