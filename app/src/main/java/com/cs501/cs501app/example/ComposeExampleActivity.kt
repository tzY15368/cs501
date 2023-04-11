package com.cs501.cs501app.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

class ComposeExampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            ScaffoldExample()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ScaffoldExample() {
        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            floatingActionButton = {
                var clickCount by remember { mutableStateOf(0) }
                ExtendedFloatingActionButton(
                    onClick = {
                        // show snackbar as a suspend function
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                "Snackbar # ${++clickCount}"
                            )
                        }
                    }
                ) { Text("Show snackbar") }
            },
            content = { innerPadding ->
                Text(
                    text = "Body content",
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .wrapContentSize()
                )
                Button(onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            "Snackbar # "
                        )
                    }
                }) {
                    Text("Button")
                }
            }
        )
    }
}