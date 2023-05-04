package com.cs501.cs501app.utils

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import com.cs501.cs501app.buotg.view.user_map.getCurrentLocation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

private const val TAG = "PollWorker"
val EVENT_NOTIFICATION_ID = 8
val EVENT_DISTANCE_NOTI_ID = 9

class PollWorker(
    private val context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    private val eventRepo = AppRepository.get().eventRepo()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private fun handleLocationUpdate(loc: Location?) {
        Log.i(TAG, "Current location: $loc")
        loc?.let {
            checkEvents(it)
        }
    }

    private fun checkEvents(loc: Location){
        // get all events for today
        coroutineScope.launch {
            // get current datetime
            val date = Date()
            val events = eventRepo.getAllEventsByDate(context, date)
            Log.i(TAG, "Events for today: ${events.size}")
            for (event in events) {
                if (event.start_time.time + event.notify_time * 60 * 1000 <= date.time) {
                    // event has not started yet
                    continue
                }
                // send notification
                Log.i(TAG, "Sending notification for event: $event")
                sendNotification(
                    context,
                    "Incoming event: ${event.event_name}",
                    "Event starts at ${event.start_time}",
                    EVENT_NOTIFICATION_ID,
                    true
                )
                if(event.latitude==0f||event.longitude==0f){
                    // no location set for event
                    continue
                }
                // check if user is within 1500m of event location
                val eventLoc = Location("event")
                eventLoc.latitude = event.latitude.toDouble()
                eventLoc.longitude = event.longitude.toDouble()
                val distance = loc.distanceTo(eventLoc)
                if (loc.distanceTo(eventLoc) <= 1500) {
                    // user is within 1500m of event location
                    Log.i(TAG, "User is within 1500m of event location")
                    sendNotification(
                        context,
                        "Travel time warning: ${event.event_name}",
                        "Event is starting soon, you may not have enough time to travel to the event location",
                        EVENT_DISTANCE_NOTI_ID,
                        true
                    )
                }
            }
        }
    }

    override suspend fun doWork(): Result {
        Log.i(TAG, "Work request triggered")
        //checkEvents()
        val res = getCurrentLocation(context) { handleLocationUpdate(it) }
        Log.i(TAG, "Work request completed: $res")
        return if (res.isSuccess) Result.success() else Result.failure()
    }
}