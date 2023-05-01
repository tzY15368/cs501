package com.cs501.cs501app.buotg.view.common

import android.util.Log
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import kotlinx.coroutines.launch

@Composable
fun Ping(modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()
    val ctx = LocalContext.current
    Button(
        modifier = modifier,
        onClick = {
        coroutineScope.launch {
            val response = AppRepository.get().ping(ctx)
            Log.d("Ping", "response: $response")
            //response?.let { TAlert.success(this@MainActivity, "Ping:${response.message}") }
        }
    }) {
        Text("Ping")
    }
}