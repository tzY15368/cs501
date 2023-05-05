package com.cs501.cs501app.buotg.view.homeScreen.sharedEvent

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.cs501.cs501app.R
import com.cs501.cs501app.buotg.database.entities.SharedEvent
import com.cs501.cs501app.buotg.database.entities.SharedEventParticipance
import com.cs501.cs501app.buotg.database.entities.Status
import com.cs501.cs501app.buotg.database.entities.User
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import com.cs501.cs501app.buotg.view.thirdParty.chatRoom.ChatApplication
import kotlinx.coroutines.launch
import java.util.*
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSharedEventHandler(
    creatingSharedEvent: MutableState<Boolean>,
    reloadSharedEvents: suspend () -> Unit,
    eventId:UUID?,
    currentUser: MutableState<User?>,
    targetApp: ChatApplication
) {
    val coroutineScope = rememberCoroutineScope()
    val sharedEventRepo = AppRepository.get().sharedEventRepo()
    val sharedEventParticipanceRepo = AppRepository.get().sharedEventParticipanceRepo()
    val newSharedEventName = remember { mutableStateOf("") }
    val newSharedEventDesc = remember { mutableStateOf("") }
    val ctx = LocalContext.current
    Dialog(onDismissRequest = { creatingSharedEvent.value = false }) {
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
                value = newSharedEventName.value,
                onValueChange = { newSharedEventName.value = it },
                label = { Text(text = stringResource(id = R.string.shared_event_name)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            TextField(
                value = newSharedEventDesc.value,
                onValueChange = { newSharedEventDesc.value = it },
                label = { Text(text = stringResource(id = R.string.shared_event_desc)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            Button(onClick = {
                Log.d("CLICKED", eventId.toString())
                Log.d("CLICKED", currentUser.toString())
                coroutineScope.launch {
                    var sharedEvent = eventId?.let {
                        currentUser.value?.let { it1 ->
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
                    // What is this?
//                    val sharedEvents_be = eventId?.let {
//                        sharedEventRepo.getAllSharedEventByEventId(it, ctx)?.shared_event
//                    } ?: listOf()
//                    Log.d("be sharedevent", sharedEvents_be.toString())
//                    Log.d("db sharedevent", sharedEvent.toString())
//                    for (se in sharedEvents_be) {
//                        var exists: Boolean = false
//                        for (ese in sharedEvents) {
//                            if (se == ese) {
//                                Log.d("found sharedevent", se.toString())
//                                exists = true
////                                        break
//                            }
//                        }
//                        if (exists == false) {
//                            sharedEvent = se
//                            Log.d("find latest sharedevent", se.toString())
//                            Log.d("1current sharedevent list", sharedEvent.toString())
////                                    sharedEventRepo.insertSharedEvent(sharedEvent)
//                            Log.d("current sharedevent list", sharedEvent.toString())
//                        }
//                    }

                    val participance = currentUser.value?.let {
                        sharedEvent?.let { it1 ->
                            SharedEventParticipance(
                                shared_event_id = it1.shared_event_id,
                                user_id = it.user_id,
                                status = Status.FAIL
                            )
                        }
                    }
                    if (participance != null) {
                        Log.d("createSharedEventPart", participance.toString())
                        sharedEventParticipanceRepo.updateParticipance(participance, ctx)
                    }

                    targetApp.create_channel("$newSharedEventName" + "_shared_events")
                    reloadSharedEvents()
                }
                creatingSharedEvent.value = false
            }) {
                Text(text = stringResource(id = R.string.create))
            }
        }
    }
}