package com.cs501.cs501app.buotg

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cs501.cs501app.buotg.database.AppRepository
import com.cs501.cs501app.buotg.homeScreen.EventTrackerViewModel
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.cs501.cs501app.buotg.bottomsheet.EventBottomSheet
import com.cs501.cs501app.buotg.homeScreen.EventTrackerFAB
import com.cs501.cs501app.buotg.homeScreen.EventTrackerList
import com.cs501.cs501app.buotg.homeScreen.EventTrackerTopAppBar

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EventTracker(
    modifier: Modifier = Modifier,
    eventTrackerViewModel: EventTrackerViewModel = viewModel(factory = viewModelFactory {
        initializer {
            EventTrackerViewModel(AppRepository.get().getEventRepository())
        }
    })
) {

    val sheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden
    )
    val scope = rememberCoroutineScope()
    val trackerState by eventTrackerViewModel.eventListStream.collectAsState(emptyList())

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
                )
            }
        }
    }
}