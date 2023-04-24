package com.cs501.cs501app.buotg.view.poll_tool

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.cs501.cs501app.buotg.database.entities.Event
import com.cs501.cs501app.buotg.database.entities.User
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import com.cs501.cs501app.buotg.database.repositories.EventRepository
import com.cs501.cs501app.buotg.database.repositories.EventRepositoryImpl

@Composable
fun calculateUserSchedule(users : List<User>) {
    val eventRepo = AppRepository.get().eventRepo()
    val ctx = LocalContext.current
    val allEventsSchedule = mutableMapOf<User, List<Event>>()
    LaunchedEffect(true) {
        for (user in users) {
            var events_schedule = eventRepo.getAllEventsByUserId(ctx, user.user_id)
            allEventsSchedule[user] = events_schedule
        }
    }

}