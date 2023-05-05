package com.cs501.cs501app.buotg.view.homeScreen.sharedEvent

import android.location.Location
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cs501.cs501app.R
import com.cs501.cs501app.buotg.database.entities.SharedEventParticipance
import com.cs501.cs501app.buotg.database.entities.Status
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import com.cs501.cs501app.buotg.view.user_map.getCurrentLocation
import com.cs501.cs501app.utils.TAlert
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun takeAttendanceBtn(sharedEventId: Int, userId: UUID, eventLocation: Location, callback: suspend () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val sharedEventParticipanceRepo = AppRepository.get().sharedEventParticipanceRepo()
    val ctx = LocalContext.current
    var hasTaken = remember { mutableStateOf(false) }
    OutlinedButton(modifier = Modifier.size(30.dp),
        border = BorderStroke(1.dp,color = Color.Black),
        enabled = !hasTaken.value,
        onClick = {
            Log.d("Try_CLICKED_ATTENDANCE", sharedEventId.toString())
            getCurrentLocation(ctx) {userLocation ->
                if (userLocation != null) {
                    if (userLocation.distanceTo(eventLocation) <= 100) {
                        Log.d("CLICKED_ATTENDANCE", sharedEventId.toString())
                        val prev_participance = SharedEventParticipance(shared_event_id = sharedEventId, user_id = userId, status = Status.FAIL)
                        coroutineScope.launch {
                            sharedEventParticipanceRepo.deleteParticipance(prev_participance,ctx)
                            callback()
                        }
                        val participance = SharedEventParticipance(shared_event_id = sharedEventId, user_id = userId, status = Status.SUCCESS)
                        coroutineScope.launch {
                            sharedEventParticipanceRepo.updateParticipance(participance,ctx)
                            callback()
                        }
                        hasTaken.value = true
                    }
                    else {
                        Log.d("CANNOT_TAKE_ATTEND", sharedEventId.toString())
                        TAlert.fail(ctx,"Away from valid scope!")
                    }
                }
            }

        }) {

        Icon(
            Icons.Outlined.Add,
            contentDescription = stringResource(id = R.string.take_attendence),
            modifier = Modifier.size(20.dp)


        )
        Text(
            text = "Take Attendance1",
            modifier = Modifier.padding(start = 8.dp)
        )
    }

}