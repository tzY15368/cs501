package com.cs501.cs501app.buotg.view.homeScreen.sharedEvent

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog

@Composable
fun TranslucentDialog(
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5F), // Set the alpha value for translucency
            contentColor = contentColorFor(MaterialTheme.colorScheme.surface)
        ) {
            content()
        }
    }
}