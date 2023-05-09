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
import com.cs501.cs501app.utils.TAlert
import com.cs501.cs501app.utils.getBGserviceState
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

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
    val loc_not_ok_txt  = stringResource(id = R.string.location_not_ok)
    Button(
        onClick = {
            Log.d("Try_CLICKED_ATTENDANCE", eventData.shared_event.shared_event_id.toString())
            suspend fun putParticipance(){
                val participance =
                    SharedEventParticipance(
                        shared_event_id = eventData.shared_event.shared_event_id,
                        user_id = userId,
                        status = Status.SUCCESS
                    )
                sharedEventParticipanceRepo.putParticipance(
                    participance,
                    ctx
                )
                reload()
            }
            coroutineScope.launch {
                val locStatus:Boolean = getBGserviceState()
                if(locStatus){
                    // we have location service active, apply location check
                    getCurrentLocation(ctx) { userLocation ->
                        var locationOK = false
                        if (eventLocation!=null && userLocation != null && userLocation.distanceTo(eventLocation) <= CHECKIN_DISTANCE) {
                            locationOK = true
                        }
                        Log.d("Try_LOCATION_CHECK", "eventLocation: $eventLocation," +
                                " userLocation: $userLocation, locationOK: $locationOK")
                        if (locationOK) {
                            runBlocking {
                                putParticipance()
                            }
                        } else {
                            TAlert.fail(ctx, loc_not_ok_txt)
                        }
                    }
                } else {
                    // we don't have location service active, skip location check
                    runBlocking {
                        putParticipance()
                    }
                }
            }
        }, //enabled = eventData.participance.status == Status.FAIL
    ) {
        Text(text = stringResource(id = R.string.take_attendence))
    }

}