package com.cs501.cs501app.buotg.view.homeScreen.bottomsheet

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.cs501.cs501app.R
import com.cs501.cs501app.buotg.view.homeScreen.bottomsheet.InputRow
import com.cs501.cs501app.buotg.database.entities.EventPriority

class SpinnerAdapter(val onPriorityChange: (Int) -> Unit) : AdapterView.OnItemSelectedListener {
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        onPriorityChange(position)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        onPriorityChange(EventPriority.Red.ordinal)
    }
}

/**
 * Composable which includes a spinner which is rendered through AndroidView
 * using Spinner view component
 */
@Composable
fun EventSpinnerRow(
    prioritySpinnerPosition: Int,
    onPriorityChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    /**Will this 低中高 affect the data in database?*/
    val eventPriorityArray =
        EventPriority.values().map { event_priority -> stringResource(event_priority.priority) }
    InputRow(inputLabel = stringResource(R.string.priority), modifier = modifier) {
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { context ->
                Spinner(context).apply {
                    adapter =
                        ArrayAdapter(
                            context,
                            R.layout.simple_spinner_dropdown_item,
                            eventPriorityArray
                        )
                }
            },
            update = { spinner ->
                spinner.setSelection(prioritySpinnerPosition)
                spinner.onItemSelectedListener = SpinnerAdapter(onPriorityChange)
            }
        )
    }
}