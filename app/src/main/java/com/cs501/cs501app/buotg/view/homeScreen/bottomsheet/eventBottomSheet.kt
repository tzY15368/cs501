package com.cs501.cs501app.buotg.view.homeScreen.bottomsheet

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import java.util.*
import androidx.compose.material.*
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.cs501.cs501app.R
import com.cs501.cs501app.buotg.database.entities.Event
import com.cs501.cs501app.buotg.database.entities.EventPriority
import com.cs501.cs501app.buotg.view.user_map.MapAddressPickerView
import com.cs501.cs501app.buotg.view.user_map.MapViewModel
import com.google.android.material.datepicker.*
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EventBottomSheet(
    event: Event,
    sheetState: ModalBottomSheetState,
    onCancel: () -> Unit,
    onSubmit: (Event) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    ModalBottomSheetLayout(
        modifier = modifier,
        sheetState = sheetState,
        sheetContent = {
            Column {
                SheetHeader()
                SheetForm(
                    event = event,
                    onCancel = onCancel,
                    onSubmit = onSubmit
                )
            }
        }
    ) {
        content()
    }
}

@Composable
fun SheetHeader(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(8.dp)) {
        Text(
            modifier = modifier.padding(8.dp),
            text = stringResource(R.string.bottom_sheet_headline),
            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
        )
        Divider()
    }
}

@Composable
fun SheetForm(
    event: Event,
    onCancel: () -> Unit,
    onSubmit: (Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    val eventName = remember(event) { mutableStateOf(event.event_name) }
    val eventDesc = remember(event) { mutableStateOf(event.desc) }
    val eventPriority = remember(event) { mutableStateOf(event.priority) }
    var startDateTime by remember(event) { mutableStateOf(event.start_time) }
    var endDateTime by remember(event) { mutableStateOf(event.end_time) }

    var latitude by remember(event) { mutableStateOf(event.latitude) }
    var longitude by remember(event) { mutableStateOf(event.longitude) }
    var address by remember(event) { mutableStateOf("") }
    var openDialog by remember(event) {
        mutableStateOf(
            false
        )
    }
    val viewModel = MapViewModel()
    Column(modifier.padding(horizontal = 16.dp)) {
        TextInputRow(
            inputLabel = stringResource(R.string.event_name),
            fieldValue = eventName.value,
            onValueChange = { name -> eventName.value = name }
        )
        Row {
            // display location info
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location",
                modifier = Modifier
                    .padding(top = 16.dp, start = 0.dp, end = 8.dp)
                    .size(28.dp)
            )
            TextField(
                value = address,
                onValueChange = { },
                enabled = false,
                maxLines = 1,
                modifier = Modifier
                    .clickable(onClick = {
                        openDialog = true
                    })
                    .fillMaxWidth()
            )
            if (openDialog) {
                AlertDialog(onDismissRequest = { openDialog = false },
                    title = { Text(text = "Select Location") },
                    confirmButton = {
                        Button(
                            onClick = {
                                latitude = viewModel.location.value.latitude.toFloat()
                                longitude = viewModel.location.value.longitude.toFloat()
                                address = viewModel.addressText.value
                                Log.d("EventBottomSheet", "address: $address")
                                Log.d("EventBottomSheet", "latitude: $latitude")
                                Log.d("EventBottomSheet", "longitude: $longitude")
                                openDialog = false
                            }) {
                            Text(text = "Save")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { openDialog = false }) {
                            Text(text = "Cancel")
                        }
                    },
                    text = { MapAddressPickerView(viewModel = viewModel) }
                )
            }
        }
        TextInputRow(
            inputLabel = stringResource(R.string.event_description),
            fieldValue = eventDesc.value,
            onValueChange = { desc -> eventDesc.value = desc }
        )
        EventSpinnerRow(
            prioritySpinnerPosition = EventPriority.values()[event.priority - 1].ordinal,
            onPriorityChange = { priority ->
                eventPriority.value = priority + 1
            }
        )
        DatePickerRow(
            inputLabel = stringResource(R.string.event_start_time),
            onStartTimeChanged = { newStartTime ->
                startDateTime = newStartTime
            },
            onEndTimeChanged = { newEndTime ->
                endDateTime = newEndTime
            }
        )

        ButtonRow(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onCancel = onCancel,
            onSubmit = {
                val updatedEvent = event.copy(
                    event_name = eventName.value,
                    desc = eventDesc.value,
                    priority = eventPriority.value,
                    latitude = latitude,
                    longitude = longitude,
                )
                onSubmit(updatedEvent)
            },
            submitButtonEnabled = event.event_name.isNotEmpty()
        )
    }
}

@Composable
fun ButtonRow(
    onCancel: () -> Unit,
    onSubmit: () -> Unit,
    submitButtonEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        OutlinedButton(
            onClick = onCancel,
            border = null
        ) {
            Text(stringResource(R.string.cancel).uppercase(Locale.getDefault()))
        }
        Button(
            onClick = onSubmit,
            enabled = submitButtonEnabled
        ) {
            Text(stringResource(R.string.save).uppercase(Locale.getDefault()))
        }
    }
}


@Composable
fun TextInputRow(
    inputLabel: String,
    fieldValue: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    InputRow(inputLabel, modifier) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = fieldValue,
            onValueChange = onValueChange,
            singleLine = true,
            maxLines = 1,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.surface
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
    }
}


@Composable
fun InputRow(
    inputLabel: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier.padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = inputLabel,
            fontWeight = FontWeight.SemiBold,
            modifier = modifier
                .weight(1f)
                .padding(end = 8.dp),
        )
        Box(modifier = Modifier.weight(2f)) {
            content()
        }
    }
}