package com.cs501.cs501app.buotg.view.homeScreen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cs501.cs501app.R
import com.cs501.cs501app.buotg.database.entities.Event
import com.cs501.cs501app.buotg.database.entities.EventPriority
import java.util.*

@Composable
fun EventTrackerList(
    events: List<Event>,
    onDelete: (Event) -> Unit,
    onUpdate: (Event) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        items(items = events) { event ->
            EventTrackerListItem(event, onDelete, onUpdate)
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
                    event_id = 1,
                    event_name = "Event 1",
                    latitude = 1234,
                    longitude = 9876,
                    start_time = Date(),
                    end_time = Date(),
                    repeat_mode = 0,
                    priority = 1,
                    desc = "Event 1 description"
                ),
                Event(
                    event_id = 2,
                    event_name = "Event 2",
                    latitude = 234567,
                    longitude = 876,
                    start_time = Date(),
                    end_time = Date(),
                    repeat_mode = 0,
                    priority = 2,
                    desc = "Event 2 description"
                ),
                Event(
                    event_id = 3,
                    event_name = "Event 3",
                    latitude = 341,
                    longitude = 7654329,
                    start_time = Date(),
                    end_time = Date(),
                    repeat_mode = 0,
                    priority = 3,
                    desc = "Event 3 description"
                ),
                Event(
                    event_id = 4,
                    event_name = "Event 4",
                    latitude = 4562,
                    longitude = 6548,
                    start_time = Date(),
                    end_time = Date(),
                    repeat_mode = 0,
                    priority = 1,
                    desc = "Event 4 description"
                ),
                Event(
                    event_id = 5,
                    event_name = "Event 5",
                    latitude = 5673,
                    longitude = 5487,
                    start_time = Date(),
                    end_time = Date(),
                    repeat_mode = 0,
                    priority = 2,
                    desc = "Event 5 description"
                )
            ),
            onDelete = {},
            onUpdate = {}
        )
    }
}