package com.cs501.cs501app.buotg.homeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cs501.cs501app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventTrackerTopAppBar(modifier: Modifier = Modifier) {
    TopAppBar(
        modifier = modifier.background(MaterialTheme.colorScheme.primary),
        title = {
            Text(
                text = stringResource(R.string.event_app_name),
                modifier = Modifier.padding(horizontal = 16.dp),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    )
}