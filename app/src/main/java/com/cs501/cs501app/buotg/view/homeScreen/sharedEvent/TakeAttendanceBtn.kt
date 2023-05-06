package com.cs501.cs501app.buotg.view.homeScreen.sharedEvent

import android.location.Location
import android.util.Log
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.cs501.cs501app.R
import com.cs501.cs501app.buotg.connection.SharedEventListItem
import com.cs501.cs501app.buotg.database.entities.SharedEventParticipance
import com.cs501.cs501app.buotg.database.entities.Status
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import com.cs501.cs501app.buotg.view.user_map.getCurrentLocation
import kotlinx.coroutines.launch
import java.util.*

val MUST_CHECK_LOCATION = false

@Composable
fun takeAttendanceBtn(
    eventData: SharedEventListItem,
    userId: UUID,
    eventLocation: Location?,
    reload: suspend () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val sharedEventParticipanceRepo = AppRepository.get().sharedEventParticipanceRepo()
    val ctx = LocalContext.current
    Button(
        onClick = {
            Log.d("Try_CLICKED_ATTENDANCE", eventData.shared_event.shared_event_id.toString())
            getCurrentLocation(ctx) { userLocation ->
                var locationOK = false
                if (eventLocation!=null && userLocation != null && userLocation.distanceTo(eventLocation) <= CHECKIN_DISTANCE) {
                    locationOK = true
                }
                if (locationOK || !MUST_CHECK_LOCATION) {
                    val participance =
                        SharedEventParticipance(
                            shared_event_id = eventData.shared_event.shared_event_id,
                            user_id = userId,
                            status = Status.SUCCESS
                        )

                    coroutineScope.launch {
                        sharedEventParticipanceRepo.putParticipance(
                            participance,
                            ctx
                        )
                        reload()
                    }
                } else {
                    println("location not ok")
                }
            }
        }, enabled = eventData.participance.status == Status.FAIL
    ) {
        Text(text = stringResource(id = R.string.take_attendence))
    }

}