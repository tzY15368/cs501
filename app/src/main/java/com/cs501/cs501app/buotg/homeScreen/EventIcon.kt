package com.cs501.cs501app.buotg.homeScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.cs501.cs501app.R
import com.cs501.cs501app.buotg.entities.EventPriority

@Composable
fun EventIcon(priority: Int, modifier: Modifier = Modifier) {
    val eventIconContentDescription = stringResource(R.string.event_priority, priority)
    Box(
        modifier.semantics {
            contentDescription = eventIconContentDescription
        }
    ) {
        Icon(
            painter = painterResource(R.drawable.event_icon),
            contentDescription = null,
            tint = EventPriority.values()[priority].color,
            modifier = Modifier.align(Alignment.Center)
        )
//        Icon(painter = painterResource(R.drawable.juice_clear_icon), contentDescription = null)
    }
}