package com.cs501.cs501app.buotg.view.bottomsheet

import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.cs501.cs501app.buotg.view.homeScreen.EventTrackerViewModel
import java.util.*
import androidx.compose.material.*
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.*
import com.cs501.cs501app.R
import com.cs501.cs501app.buotg.database.entities.Event
import com.cs501.cs501app.buotg.database.entities.EventPriority
import com.google.android.material.datepicker.*
import java.time.LocalDate

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EventBottomSheet(
    eventTrackerViewModel: EventTrackerViewModel,
    sheetState: ModalBottomSheetState,
    onCancel: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val event by eventTrackerViewModel.currentEventStream.collectAsState()
    ModalBottomSheetLayout(
        modifier = modifier,
        sheetState = sheetState,
        sheetContent = {
            Column {
                SheetHeader()
                SheetForm(
                    event = event,
                    onUpdateEvent = eventTrackerViewModel::updateCurrentEvent,
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
    onUpdateEvent: (Event) -> Unit,
    onCancel: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.padding(horizontal = 16.dp)) {
        TextInputRow(
            inputLabel = stringResource(R.string.event_name),
            fieldValue = event.event_name,
            onValueChange = { name -> onUpdateEvent(event.copy(event_name = name)) }
        )
        TextInputRow(
            inputLabel = stringResource(R.string.event_description),
            fieldValue = event.desc,
            onValueChange = { description -> onUpdateEvent(event.copy(desc = description)) }
        )
        EventSpinnerRow(
            prioritySpinnerPosition = EventPriority.values()[event.priority - 1].ordinal,
            onPriorityChange = { priority ->
                onUpdateEvent(event.copy(priority = priority + 1))
            }
        )
        DatePickerRow(
            inputLabel = stringResource(R.string.event_start_time)
        )

        ButtonRow(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onCancel = onCancel,
            onSubmit = onSubmit,
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