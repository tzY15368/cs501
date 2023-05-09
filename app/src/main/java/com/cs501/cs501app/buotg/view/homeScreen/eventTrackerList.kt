package com.cs501.cs501app.buotg.view.homeScreen

import android.location.Location
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cs501.cs501app.R
import com.cs501.cs501app.buotg.CustomText
import com.cs501.cs501app.buotg.database.entities.*
import com.cs501.cs501app.buotg.view.user_map.BU_LOCATION_PREFIX
import com.cs501.cs501app.buotg.view.user_map.launchMap
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventTrackerList(
    events: List<Event>,
    onDelete: (Event) -> Unit,
    onUpdate: (Event) -> Unit,
    onShowSharedEvents: (Event) -> Unit,
    modifier: Modifier = Modifier
) {
    val (today, past, future) = divide_events(events)
    Column(modifier = modifier) {
        ScaffoldList(
            text = stringResource(id = R.string.past),
            events = past,
            isExpanded = false,
            onDelete = onDelete,
            onUpdate = onUpdate,
            onShowSharedEvents = onShowSharedEvents
        )
        ScaffoldList(
            text = stringResource(id = R.string.today),
            events = today,
            isExpanded = true,
            onDelete = onDelete,
            onUpdate = onUpdate,
            onShowSharedEvents = onShowSharedEvents
        )
        ScaffoldList(
            text = stringResource(id = R.string.future),
            events = future,
            isExpanded = false,
            onDelete = onDelete,
            onUpdate = onUpdate,
            onShowSharedEvents = onShowSharedEvents
        )
    }

}

@Composable
fun ScaffoldList(
    text: String,
    events: List<Event>,
    isExpanded: Boolean,
    onDelete: (Event) -> Unit,
    onUpdate: (Event) -> Unit,
    onShowSharedEvents: (Event) -> Unit
) {
    var isListExpanded by remember { mutableStateOf(isExpanded) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { isListExpanded = !isListExpanded }
    ) {
        CustomText(
            text = text,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp)
        )
        Icon(
            painter = painterResource(if (isListExpanded) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down),
            contentDescription = "Expand list",
            modifier = Modifier.padding(end = 8.dp)
        )
    }
    if (isListExpanded) {
        if (events.isEmpty()) {
            // center text
            //@Todo:What is the $text here?
            val temp = stringResource(id = R.string.no_such_events) + ": " + text
            CustomText(
                text = temp,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                color = Color.Gray,
                fontWeight = FontWeight.Bold
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(items = events) { event ->
                    EventTrackerListItem(event, onDelete, onUpdate, onShowSharedEvents)
                }
            }
        }
    }
}

@Composable
fun EventTrackerListItem(
    event: Event,
    onDelete: (Event) -> Unit,
    onUpdate: (Event) -> Unit,
    onShowSharedEvents: (Event) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onUpdate(event)
            }
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        EventIcon(event.priority - 1)
        EventDetails(event, modifier.weight(1f))
        IconButton(onClick = { onShowSharedEvents(event) }) {
            Icon(
                Icons.Default.Share,
                contentDescription = stringResource(id = R.string.show_shared)
            )
        }

//        val customLatitude = 37.4219983
//        val customLongitude = -122.084

//        val customLocation1 = Location("CustomProvider1")
//        customLocation1.latitude = USER_LATITUDE_VAL_FROM
//        customLocation1.longitude = USER_LONGITUDE_VAL_FROM
//        val customLocation2 = Location("CustomProvider1")
//        customLocation2.latitude = USER_LATITUDE_VAL_TO
//        customLocation2.longitude = USER_LONGITUDE_VAL_TO
        // try to extract the location from event desc where it is wrapped by < >
        val pattern = "<(.*?)>".toRegex()
        val matchResult = pattern.find(event.desc)
        val location = matchResult?.value?.removePrefix("<")?.removeSuffix(">") ?: ""
        if ((event.longitude != 0f && event.latitude != 0f)) {
            val target = Location("target")
            target.latitude = event.latitude.toDouble()
            target.longitude = event.longitude.toDouble()
            val ctx = LocalContext.current
            IconButton(onClick = {
                println("got target location $target")
                launchMap(ctx, target)

            }) {
                Icon(Icons.Default.LocationOn, contentDescription = "Map")
            }
        } else if (location != "") {
            val ctx = LocalContext.current
            IconButton(onClick = {
                println("got location $location")
                launchMap(ctx, BU_LOCATION_PREFIX + location)
            }) {
                Icon(Icons.Default.LocationOn, contentDescription = "Map")
            }
        }
        DeleteButton(
            onDelete = {
                onDelete(event)
            },
            modifier = Modifier.align(Alignment.Top)
        )
    }
}

// check if the event is on today
fun is_today(event: Event): Boolean {
    val today = Calendar.getInstance()
    val event_date = Calendar.getInstance()
    // if the event starts exactly today
    event_date.time = event.start_time
    if (event_date.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
        event_date.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
        event_date.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)
    )
        return true
    // if the event ends exactly today
    event_date.time = event.end_time
    if (event_date.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
        event_date.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
        event_date.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)
    )
        return true
    // if the event repeats on today
    if (event.repeat_mode != 0 && event.start_time.before(today.time) && event.end_time.after(today.time)) {
        val repeat = event.repeat_mode  // repeat is an int indicates how many days to repeat
        // convert to the 00:00:00 of the day
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)
        val event_copy = Calendar.getInstance()
        event_copy.time = event.start_time
        event_copy.set(Calendar.HOUR_OF_DAY, 0)
        event_copy.set(Calendar.MINUTE, 0)
        event_copy.set(Calendar.SECOND, 0)
        event_copy.set(Calendar.MILLISECOND, 0)
        val diff = today.timeInMillis - event_copy.timeInMillis
        // days is the number of days between today and the event start date (), rounding up
        val days = diff / (1000 * 60 * 60 * 24) + 1
        Log.d("is_today:event", event.start_time.toString())
        Log.d("is_today:days", days.toString())
        if (days % repeat == 0L)
            return true
    }
    return false
}


// divide events by today, past, future
fun divide_events(events: List<Event>): Triple<List<Event>, List<Event>, List<Event>> {
    val today = Calendar.getInstance()
    val past = mutableListOf<Event>()
    val future = mutableListOf<Event>()
    val today_events = mutableListOf<Event>()
    for (event in events) {
        val event_date = Calendar.getInstance()
        event_date.time = event.start_time
        Log.d("divide_events:event", event_date.toString())
        Log.d("divide_events:today", today.toString())
        if (is_today(event)
        ) {
            today_events.add(event)
        } else {
            if (event_date.before(today)) {
                past.add(event)
            }
            event_date.time = event.end_time
            if (event_date.after(today)){
                future.add(event)
            }
        }
    }
    Log.d("divide_events:events", events.size.toString())
    Log.d("divide_events:today_events", today_events.size.toString())
    Log.d("divide_events:past", past.size.toString())
    Log.d("divide_events:future", future.size.toString())
    return Triple(today_events, past, future)
}

@Composable
fun EventDetails(event: Event, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = event.event_name,
            style = androidx.compose.material.MaterialTheme.typography.h5.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E2E2E),
            )
        )
        val eventPosition = stringResource(id = R.string.event_position)
        val timeSlot = stringResource(id = R.string.time_slot)
        val priorityName = stringResource(id = R.string.priority_name)
        Text(
            text = "$eventPosition: ${event.latitude}, ${event.longitude}",
            style = androidx.compose.material.MaterialTheme.typography.body1
        )
        Text(
            text = "$timeSlot: ${event.start_time} - ${event.end_time} (${event.repeat_mode})",
            style = androidx.compose.material.MaterialTheme.typography.body1
        )
        Text(
            text = "$priorityName: ${stringResource(EventPriority.values()[event.priority - 1].priority)}",
            color = EventPriority.values()[event.priority - 1].color,
            style = androidx.compose.material.MaterialTheme.typography.body1
        )
    }

}

@Composable
fun DeleteButton(onDelete: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(
        onClick = { onDelete() },
    ) {
        Icon(
            modifier = modifier,
            painter = painterResource(R.drawable.delete_icon),
            contentDescription = stringResource(R.string.delete)
        )
    }
}

//@Composable
//fun SharedEventButton(onClic: () -> Unit, modifier: Modifier = Modifier) {
//    IconButton(
//        onClick = { onDelete() },
//    ) {
//        Icon(
//            modifier = modifier,
//            painter = painterResource(R.drawable.delete_icon),
//            contentDescription = stringResource(R.string.delete)
//        )
//    }
//}
//@Preview
//@Composable
//fun EventTrackerListPreview() {
//    val context = LocalContext.current
//    MaterialTheme {
//        EventTrackerList(
//            events = listOf(
//                Event(
//                    event_id = UUID.randomUUID(),
//                    event_name = "Event 1",
//                    latitude = 1234,
//                    longitude = 9876,
//                    start_time = Date(),
//                    end_time = Date(),
//                    repeat_mode = 0,
//                    priority = 1,
//                    desc = "Event 1 description",
//                    created_by = UUID.randomUUID(),
//                    notify_time = 1
//                ),
//                Event(
//                    event_id = UUID.randomUUID(),
//                    event_name = "Event 2",
//                    latitude = 234567,
//                    longitude = 876,
//                    start_time = Date(),
//                    end_time = Date(),
//                    repeat_mode = 0,
//                    priority = 2,
//                    desc = "Event 2 description",
//                    created_by = UUID.randomUUID(),
//                    notify_time = 1
//                ),
//                Event(
//                    event_id = UUID.randomUUID(),
//                    event_name = "Event 3",
//                    latitude = 341,
//                    longitude = 7654329,
//                    start_time = Date(),
//                    end_time = Date(),
//                    repeat_mode = 0,
//                    priority = 3,
//                    desc = "Event 3 description",
//                    created_by = UUID.randomUUID(),
//                    notify_time = 1
//                ),
//                Event(
//                    event_id = UUID.randomUUID(),
//                    event_name = "Event 4",
//                    latitude = 4562,
//                    longitude = 6548,
//                    start_time = Date(),
//                    end_time = Date(),
//                    repeat_mode = 0,
//                    priority = 1,
//                    desc = "Event 4 description",
//                    created_by = UUID.randomUUID(),
//                    notify_time = 1
//                ),
//                Event(
//                    event_id = UUID.randomUUID(),
//                    event_name = "Event 5",
//                    latitude = 5673,
//                    longitude = 5487,
//                    start_time = Date(),
//                    end_time = Date(),
//                    repeat_mode = 0,
//                    priority = 2,
//                    desc = "Event 5 description",
//                    created_by = UUID.randomUUID(),
//                    notify_time = 1
//                )
//            ),
//            onDelete = {},
//            onUpdate = {},
//            onShowSharedEvents = { event ->
//                val intent = Intent(context, SharedEventActivity::class.java)
//                intent.putExtra("eventId", event.event_id.toString())
//                context.startActivity(intent)
//            }
//        )
//    }
//}