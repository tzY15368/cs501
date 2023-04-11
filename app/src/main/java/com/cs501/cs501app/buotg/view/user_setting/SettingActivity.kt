package com.cs501.cs501app.buotg.view.user_setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.cs501.cs501app.buotg.database.SyncRepo
import com.cs501.cs501app.buotg.database.entities.User
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import com.cs501.cs501app.buotg.view.user_setup.LoginRegister
import com.cs501.cs501app.buotg.view.user_setup.SetupActivity
import com.cs501.cs501app.buotg.view.user_setup.StuLinkImport
import com.cs501.cs501app.utils.GenericTopAppBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        val page = listOf("Import from Student Link","User Info Edit")
        val (step, setStep) = remember { mutableStateOf(0) }
        val title = page[step]
        val userRepo = AppRepository.get().userRepo()
        val currentUser = remember { mutableStateOf<User?>(null) }
        val coroutineScope = rememberCoroutineScope()
        // State to track whether sync is in progress
        val syncInProgress = remember { mutableStateOf(false) }

        LaunchedEffect(key1 = Unit) {
            currentUser.value = withContext(Dispatchers.IO) {
                userRepo.getCurrentUser()
            }
        }
        // callback for when a step is done
        val stepDone: () -> Unit = {
            var msg = if (step == 0){
                "Import Student Link Done"
            }else{
                "Update User Info Done"
            }
            Log.d("SettingActivity", msg)
        }
        Scaffold(
            topBar = {
                GenericTopAppBar(title = title)
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                when (step) {
                    0 -> StuLinkImport(stepDone)
                    1 -> UserInfoEdit(stepDone,this@SettingActivity)
                    2 -> LoginRegister(stepDone,"1")
                }
                // spacer to keep things to the bottom
                // https://stackoverflow.com/questions/70904979/how-align-to-bottom-a-row-in-jetpack-compose
                Spacer(modifier = Modifier.weight(1f))
                // row of buttons, sticks to the bottom
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            setStep(0)
                        },
                        // Disable button if sync is in progress
                        enabled = !syncInProgress.value && currentUser.value!=null
                    ) {
                        Text(text = "Goto Student Link")
                    }
                    Button(
                        onClick = {
                            setStep(1)
                        },
                        // Disable button if sync is in progress
                        enabled = !syncInProgress.value && currentUser.value!=null
                    ) {
                        Text(text = "Goto UserInfo Edit")
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
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
                        enabled = !syncInProgress.value && currentUser.value!=null
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
                        enabled = !syncInProgress.value && currentUser.value!=null
                    ){
                        Text(text = "Logout")
                    }
                    LoginRegisterEntry(syncInProgress, currentUser.value==null)
                }
            }
        }
    }

    @Composable
    fun LoginRegisterEntry(syncInProgress: MutableState<Boolean>, visible : Boolean) {
        val simpleName = "Login/Register"
        Button(
            onClick = {
                val intent = Intent(this, SetupActivity::class.java)
                startActivity(intent)
            },
            // Disable button if sync is in progress
            enabled = !syncInProgress.value,
            modifier = Modifier.alpha(if(visible) 1f else 0f)
        ) {
            Text(text = "Goto $simpleName")
        }

    }
}