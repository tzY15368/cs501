package com.cs501.cs501app.buotg.view.homeScreen.bottomsheet

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.unit.dp
import com.cs501.cs501app.R
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*

@Composable
fun DatePickerRow(
    inputLabel: String,
    modifier: Modifier = Modifier,
    onStartTimeChanged: (Date) -> Unit,
    onEndTimeChanged: (Date) -> Unit
) {
    var context = LocalContext.current
    var calendar = Calendar.getInstance()
    val year = calendar[Calendar.YEAR]
    val month = calendar[Calendar.MONTH]
    val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]

    var selectedStartDateText by remember { mutableStateOf("") }
    var selectedStartTimeText by remember { mutableStateOf("") }
    var selectedEndDateText by remember { mutableStateOf("") }
    var selectedEndTimeText by remember { mutableStateOf("") }

    val datePickerStart = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            selectedStartDateText = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
            calendar.set(selectedYear, selectedMonth, selectedDayOfMonth, hour, minute)
            onStartTimeChanged(calendar.time)
        }, year, month, dayOfMonth
    )
    datePickerStart.datePicker.minDate = calendar.timeInMillis
    val timePickerStart = TimePickerDialog(
        context,
        { _, selectedHour: Int, selectedMinute: Int ->
            selectedStartTimeText = "$selectedHour:$selectedMinute"
            calendar.set(year, month, dayOfMonth, selectedHour, selectedMinute)
            onStartTimeChanged(calendar.time)
        }, hour, minute, false
    )

    val datePickerEnd = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            selectedEndDateText = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
            calendar.set(selectedYear, selectedMonth, selectedDayOfMonth, hour, minute)
            onEndTimeChanged(calendar.time)
        }, year, month, dayOfMonth
    )
    datePickerEnd.datePicker.minDate = calendar.timeInMillis
    val timePickerEnd = TimePickerDialog(
        context,
        { _, selectedHour: Int, selectedMinute: Int ->
            selectedEndTimeText = "$selectedHour:$selectedMinute"
            calendar.set(year, month, dayOfMonth, selectedHour, selectedMinute)
            onEndTimeChanged(calendar.time)
        }, hour, minute, false
    )
        Row(
            modifier = modifier
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column() {
                Text(
                    text = if (selectedStartDateText.isNotEmpty()) {
                        "$selectedStartDateText - $selectedStartTimeText"
                    } else {
                        stringResource(id = R.string.pick_a_start)
                    }
                )
                Log.d("DatePickerRow", "Selected start time is $selectedStartDateText")
                Button(
                    onClick = {
                        timePickerStart.show()
                        datePickerStart.show()
                    }
                ) {
                    Text(text = stringResource(id = R.string.select_start))
                }
            }
            Column() {
                Text(
                    text = if (selectedEndDateText.isNotEmpty()) {
                        "$selectedEndDateText - $selectedEndTimeText"
                    } else {
                        stringResource(id = R.string.pick_a_end)
                    }
                )
                Log.d("DatePickerRow", "Selected end time is $selectedEndDateText")
                Button(
                    onClick = {
                        timePickerEnd.show()
                        datePickerEnd.show()
                    }
                ) {
                    Text(text = stringResource(id = R.string.select_end))
                }
            }
        }
    }
