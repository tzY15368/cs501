package com.cs501.cs501app.buotg.view.homeScreen

import android.content.res.Resources
import android.util.Log
import androidx.compose.ui.res.stringResource
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs501.cs501app.R
import com.cs501.cs501app.buotg.database.entities.CURRENT_USER_ID
import com.cs501.cs501app.buotg.database.repositories.EventRepository
import com.cs501.cs501app.buotg.database.entities.Event
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class EventTrackerViewModel(private val eventRepository: EventRepository) : ViewModel() {
    private val CURRENT_UID: UUID by lazy {
        runBlocking { setCURRENT_UID() }
    }
    private var emptyEvent = Event(event_id = UUID.randomUUID(), event_name = "Empty Event", latitude = 1234, longitude = 9876, start_time = Date(), end_time = Date(),
        repeat_mode = 0, priority = 1, desc = "Empty Event description", created_by = CURRENT_UID, notify_time = 0)
    private val _currentEventStream = MutableStateFlow(emptyEvent)
    val currentEventStream: StateFlow<Event> = _currentEventStream
    val eventListStream: Flow<List<Event>> = eventRepository.eventStream

    suspend fun setCURRENT_UID(): UUID {
        return AppRepository.get().userRepo().getCurrentUser()!!.user_id
    }
    fun newEmptyEvent() {
        emptyEvent = Event(event_id = UUID.randomUUID(), event_name = "Empty Event", latitude = 1234, longitude = 9876, start_time = Date(), end_time = Date(),
            repeat_mode = 0, priority = 1, desc = "Empty Event description", created_by = CURRENT_UID, notify_time = 0)
    }
    fun resetCurrentEvent() {
        newEmptyEvent()
        _currentEventStream.update { emptyEvent }
    }
    fun updateCurrentEvent(event: Event) {
        _currentEventStream.update { event }
    }

    fun saveEvent(ctx : Context) = viewModelScope.launch {
        if (_currentEventStream.value.event_id == emptyEvent.event_id) {
            newEmptyEvent()
            eventRepository.addEvent(ctx, _currentEventStream.value)
        } else {
            eventRepository.updateEvent(ctx, _currentEventStream.value)
        }
    }

    fun deleteEvent(ctx : Context, event: Event) = viewModelScope.launch {
        eventRepository.deleteEvent(ctx, event)
    }
}