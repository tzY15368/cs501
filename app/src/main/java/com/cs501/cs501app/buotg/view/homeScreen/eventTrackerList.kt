package com.cs501.cs501app.buotg.view.homeScreen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cs501.cs501app.R
import com.cs501.cs501app.buotg.database.entities.Event
import com.cs501.cs501app.buotg.database.entities.EventPriority
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventTrackerList(
    events: List<Event>,
    onDelete: (Event) -> Unit,
    onUpdate: (Event) -> Unit,
    modifier: Modifier = Modifier
) {
    val (today, past, future) = divide_events(events)
    Column(modifier = modifier) {
        ScaffoldList(
            text = "Past",
            events = past,
            isExpanded = false,
            onDelete = onDelete,
            onUpdate = onUpdate
        )
        ScaffoldList(
            text = "Today",
            events = today,
            isExpanded = true,
            onDelete = onDelete,
            onUpdate = onUpdate
        )
        ScaffoldList(
            text = "Future",
            events = future,
            isExpanded = false,
            onDelete = onDelete,
            onUpdate = onUpdate
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
) {
    var isListExpanded by remember { mutableStateOf(isExpanded) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { isListExpanded = !isListExpanded }
    ) {
        Text(
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
            Text(
                text = "No $text events yet.",
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
                    EventTrackerListItem(event, onDelete, onUpdate)
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
        DeleteButton(
            onDelete = {
                onDelete(event)
            },
            modifier = Modifier.align(Alignment.Top)
        )
    }
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
        if (event_date.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
            event_date.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
            event_date.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)
        ) {
            today_events.add(event)
        } else if (event_date.before(today)) {
            past.add(event)
        } else {
            future.add(event)
        }
    }
    return Triple(today_events, past, future)
}

@Composable
fun EventDetails(event: Event, modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.Top) {
        Text(
            text = event.event_name,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
        )
        Text("event_position: " + event.latitude.toString() + ", " + event.longitude.toString())
        Text(text = "time_slot: " + event.start_time.toString() + " - " + event.end_time.toString() + " (" + event.repeat_mode.toString() + ")")
        Text(
            text = "priority: " + stringResource(EventPriority.values()[event.priority - 1].priority),
            color = EventPriority.values()[event.priority - 1].color
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

@Preview
@Composable
fun EventTrackerListPreview() {

    MaterialTheme {
        EventTrackerList(
            events = listOf(
                Event(
                    event_id = UUID.randomUUID(),
                    event_name = "Event 1",
                    latitude = 1234,
                    longitude = 9876,
                    start_time = Date(),
                    end_time = Date(),
                    repeat_mode = 0,
                    priority = 1,
                    desc = "Event 1 description",
                    created_by = UUID.randomUUID(),
                    notify_time = 1
                ),
                Event(
                    event_id = UUID.randomUUID(),
                    event_name = "Event 2",
                    latitude = 234567,
                    longitude = 876,
                    start_time = Date(),
                    end_time = Date(),
                    repeat_mode = 0,
                    priority = 2,
                    desc = "Event 2 description",
                    created_by = UUID.randomUUID(),
                    notify_time = 1
                ),
                Event(
                    event_id = UUID.randomUUID(),
                    event_name = "Event 3",
                    latitude = 341,
                    longitude = 7654329,
                    start_time = Date(),
                    end_time = Date(),
                    repeat_mode = 0,
                    priority = 3,
                    desc = "Event 3 description",
                    created_by = UUID.randomUUID(),
                    notify_time = 1
                ),
                Event(
                    event_id = UUID.randomUUID(),
                    event_name = "Event 4",
                    latitude = 4562,
                    longitude = 6548,
                    start_time = Date(),
                    end_time = Date(),
                    repeat_mode = 0,
                    priority = 1,
                    desc = "Event 4 description",
                    created_by = UUID.randomUUID(),
                    notify_time = 1
                ),
                Event(
                    event_id = UUID.randomUUID(),
                    event_name = "Event 5",
                    latitude = 5673,
                    longitude = 5487,
                    start_time = Date(),
                    end_time = Date(),
                    repeat_mode = 0,
                    priority = 2,
                    desc = "Event 5 description",
                    created_by = UUID.randomUUID(),
                    notify_time = 1
                )
            ),
            onDelete = {},
            onUpdate = {}
        )
    }
}