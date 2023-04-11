package com.cs501.cs501app.buotg.view.user_setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
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

class SettingActivity() : AppCompatActivity() {

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
        val currentUser = remember { mutableStateOf<User?>(null) }
        val userRepo = AppRepository.get().userRepo()

        val ctx = LocalContext.current
        // State to track whether sync is in progress
        val syncInProgress = remember { mutableStateOf(false) }
        val foldViewModel: FoldViewModel = viewModel(factory = viewModelFactory {
            initializer {
                FoldViewModel(listOf(false, false, false, false))
            }
        })
        val foldStateList = foldViewModel.showListFlow
        // Collect StateFlow outside of composable
        val foldStates = foldStateList.map { it.collectAsState() }

        LaunchedEffect(true) {
            currentUser.value = withContext(Dispatchers.IO) {
                userRepo.getCurrentUser()
            }
        }
        // callback for when a step is done
        val stepDone: () -> Unit = {
            Log.d("Settings", "done")
            foldViewModel.foldAll()
        }
        val userN = if (currentUser.value == null) "No logon" else currentUser.value!!.full_name
        Scaffold(
            topBar = { GenericTopAppBar(title = "Settings: $userN") }
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
                Row {
                    FoldableListItem(
                        0,
                        foldViewModel,
                        foldStates[0],
                        headlineText = { Text(text = "Sync data") },
                        leadingContent = { Icon(Icons.Filled.Refresh, contentDescription = null) },
                        foldedContent = { SyncBtn(loading = syncInProgress, user = currentUser) },
                    )
                }
                Row {
                    FoldableListItem(
                        1,
                        foldViewModel,
                        foldStates[1],
                        headlineText = { Text(text = "Import from Student Link") },
                        leadingContent = { Icon(Icons.Filled.AddCircle, contentDescription = null) },
                        foldedContent = { StuLinkImport(stepDone) },
                    )
                }
                Row {
                    FoldableListItem(
                        2,
                        foldViewModel,
                        foldStates[2],
                        headlineText = { Text(text = "Edit user info") },
                        leadingContent = { Icon(Icons.Filled.Edit, contentDescription = null) },
                        foldedContent = { UserInfoEdit(context = ctx, done = stepDone) },
                    )
                }
                val headText = if (currentUser.value == null) "Login/register" else "Logout"
                Row {
                    FoldableListItem(
                        3,
                        foldViewModel,
                        foldStates[3],
                        headlineText = { Text(text = headText) },
                        leadingContent = { Icon(Icons.Filled.Person, contentDescription = null) },
                        foldedContent = {
                            if (currentUser.value == null) {
                                LoginRegister(stepDone)
                            } else {
                                LogoutButton(user = currentUser, loading = syncInProgress)
                            }
                        },
                    )
                }
            }

        }
    }
}

@Composable
fun LogoutButton(loading: MutableState<Boolean>, user: MutableState<User?>) {
    val userRepo = AppRepository.get().userRepo()
    val coroutineScope = rememberCoroutineScope()
    val ctx = LocalContext.current
    Button(
        onClick = {
            //clear current user session
            coroutineScope.launch {
                Log.d("SettingActivity", "User Logout")
                userRepo.logout()
                // Navigate back to the setup activity
                val intent = Intent(ctx, SetupActivity::class.java)
                ctx.startActivity(intent)
            }
        },
        // Disable button if sync is in progress
        enabled = !loading.value && user.value != null
    ) {
        Text(text = "Logout")
    }
}

@Composable
fun SyncBtn(loading: MutableState<Boolean>, user: MutableState<User?>) {
    val coroutineScope = rememberCoroutineScope()
    val ctx = LocalContext.current
    Button(
        onClick = {
            // Launch a coroutine to perform sync
            coroutineScope.launch {
                // Set sync in progress to true
                loading.value = true
                // Add a delay of 1 second to simulate sync
                delay(1000)
                SyncRepo().sync(ctx)
                // Set sync in progress to false
                loading.value = false
            }
        },
        // Disable button if sync is in progress
        enabled = !loading.value && user.value != null
    ) {
        if (loading.value) {
            // Show a loading indicator if sync is in progress
            CircularProgressIndicator()
        } else {
            Text(text = "Sync")
        }
    }
}