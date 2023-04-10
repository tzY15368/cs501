package com.cs501.cs501app.buotg.view.user_setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cs501.cs501app.buotg.database.SyncRepo
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import com.cs501.cs501app.buotg.view.user_setup.SetupActivity
import com.cs501.cs501app.utils.GenericTopAppBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SettingActivity() : AppCompatActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                RenderScaffold()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun RenderScaffold() {
        val userRepo = AppRepository.get().userRepo()
        val coroutineScope = rememberCoroutineScope()
        // State to track whether sync is in progress
        val syncInProgress = remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                GenericTopAppBar()
            },
            content = { innerPadding ->
                // center the column
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(16.dp)
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    StudentLinkEntry(syncInProgress)
                    UserInfoEditEntry(syncInProgress)
                    Button(
                        onClick = {
                            // Launch a coroutine to perform sync
                            coroutineScope.launch {
                                // Set sync in progress to true
                                syncInProgress.value = true
                                // Add a delay of 1 second to simulate sync
                                delay(1000)
                                SyncRepo().sync(applicationContext)
                                // Set sync in progress to false
                                syncInProgress.value = false
                            }
                        },
                        // Disable button if sync is in progress
                        enabled = !syncInProgress.value
                    ){
                        if (syncInProgress.value) {
                            // Show a loading indicator if sync is in progress
                            CircularProgressIndicator()
                        } else {
                            Text(text = "Sync")
                        }
                    }

                    Button(
                        onClick = {
                            //clear current user session
                            coroutineScope.launch {
                                Log.d("SettingActivity", "User Logout")
                                userRepo.logout()
                            }

                            // Navigate back to the setup activity
                            val intent = Intent(this@SettingActivity, SetupActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        },
                        // Disable button if sync is in progress
                        enabled = !syncInProgress.value
                    ){
                        Text(text = "Logout")
                    }
                }
            }
        )
    }





    @Composable
    fun StudentLinkEntry(syncInProgress: MutableState<Boolean>) {
        val simpleName = "Student Link"
        Button(
            onClick = {
                val intent = Intent(this, StudentLinkActivity::class.java)
                startActivity(intent)
            },
            // Disable button if sync is in progress
            enabled = !syncInProgress.value
        ) {
            Text(text = "Goto $simpleName")
        }

    }

    @Composable
    fun UserInfoEditEntry(syncInProgress: MutableState<Boolean>) {
        val simpleName = "UserInfo Edit"
        Button(
            onClick = {
                val intent = Intent(this, UserInfoEditActivity::class.java)
                startActivity(intent)
            },
            // Disable button if sync is in progress
            enabled = !syncInProgress.value
        ) {
            Text(text = "Goto $simpleName")
        }

    }
}