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
import com.cs501.cs501app.buotg.database.entities.EventRepeatMode

class RepeatSpinnerAdapter(val onRepeatModeChange: (Int) -> Unit) : AdapterView.OnItemSelectedListener {
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        onRepeatModeChange(position)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        onRepeatModeChange(EventRepeatMode.NoRepeat.ordinal)
    }
}

/**
 * Composable which includes a spinner which is rendered through AndroidView
 * using Spinner view component
 */
@Composable
fun RepeatSpinnerRow(
    repeatSpinnerPosition: Int,
    onRepeatModeChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val eventRepeatArray =
        EventRepeatMode.values().map { event_repeat -> stringResource(event_repeat.repeatModeString) }
    InputRow(inputLabel = stringResource(R.string.repeat_mode), modifier = modifier) {
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { context ->
                Spinner(context).apply {
                    adapter =
                        ArrayAdapter(
                            context,
                            R.layout.simple_spinner_dropdown_item,
                            eventRepeatArray
                        )
                }
            },
            update = { spinner ->
                spinner.setSelection(repeatSpinnerPosition)
                spinner.onItemSelectedListener = RepeatSpinnerAdapter(onRepeatModeChange)
            }
        )
    }
}