package com.cs501.cs501app.buotg.view.homeScreen

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import com.cs501.cs501app.buotg.view.homeScreen.EventTrackerViewModel
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.cs501.cs501app.buotg.view.bottomsheet.EventBottomSheet
import com.cs501.cs501app.buotg.view.homeScreen.EventTrackerFAB
import com.cs501.cs501app.buotg.view.homeScreen.EventTrackerList
import com.cs501.cs501app.buotg.view.homeScreen.EventTrackerTopAppBar
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EventTracker(
    modifier: Modifier = Modifier,
    eventTrackerViewModel: EventTrackerViewModel = viewModel(factory = viewModelFactory {
        initializer {
            EventTrackerViewModel(AppRepository.get().eventRepo())
        }
    })
) {

    val sheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden
    )
    val scope = rememberCoroutineScope()
    val trackerState by eventTrackerViewModel.eventListStream.collectAsState(emptyList())
    val context = LocalContext.current
    EventBottomSheet(
        eventTrackerViewModel = eventTrackerViewModel,
        modifier = modifier,
        onCancel = {
            scope.launch {
                sheetState.hide()
            }
        },
        onSubmit = {
            eventTrackerViewModel.saveEvent()
            scope.launch {
                sheetState.hide()
            }
        },
        sheetState = sheetState
    )
    {
        Scaffold(
            topBar = {
                EventTrackerTopAppBar()
            },
            floatingActionButton = {
                EventTrackerFAB(
                    onClick = {
                        eventTrackerViewModel.resetCurrentEvent()
                        scope.launch { sheetState.show() }
                    }
                )
            }
        ) { contentPadding ->
            Column(Modifier.padding(contentPadding)) {
                EventTrackerList(
                    events = trackerState,
                    onDelete = { event -> eventTrackerViewModel.deleteEvent(event) },
                    onUpdate = { event ->
                        eventTrackerViewModel.updateCurrentEvent(event)
                        scope.launch {
                            sheetState.show()
                        }
                    },
                    onShowSharedEvents = { event ->
                        val intent = Intent(context, SharedEventActivity::class.java)
                        intent.putExtra("eventId", event.event_id.toString())
                        context.startActivity(intent)

                    },
                )
            }
        }
    }
}