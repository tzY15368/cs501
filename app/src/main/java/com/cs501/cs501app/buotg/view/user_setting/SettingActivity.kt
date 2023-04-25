package com.cs501.cs501app.buotg.view.user_setting

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.cs501.cs501app.R
import com.cs501.cs501app.buotg.CustomButton
import com.cs501.cs501app.buotg.CustomText
import com.cs501.cs501app.buotg.database.SyncRepo
import com.cs501.cs501app.buotg.database.entities.User
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import com.cs501.cs501app.buotg.view.user_setup.LoginRegister
import com.cs501.cs501app.buotg.view.user_setup.SetupActivity
import com.cs501.cs501app.buotg.view.user_setup.StuLinkImport
import com.cs501.cs501app.utils.GenericTopAppBar
import kotlinx.coroutines.*
import java.util.*


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
        val ctx = LocalContext.current
        val userRepo = AppRepository.get().userRepo()
        val coroutineScope = rememberCoroutineScope()

        val currentUser = remember { mutableStateOf<User?>(null) }
        // State to track whether sync is in progress
        val syncInProgress = remember { mutableStateOf(false) }

        // ViewModel to track fold state
        val foldViewModel: FoldViewModel = viewModel(factory = viewModelFactory {
            initializer {
                FoldViewModel(listOf(false, false, false, false, false))
            }
        })
        val foldStateList = foldViewModel.showListFlow
        // Collect StateFlow outside of composable
        val foldStates = foldStateList.map { it.collectAsState() }
        val languages = listOf("English","简体中文","Español")

        fun updateData(){
            coroutineScope.launch {
                currentUser.value = withContext(Dispatchers.IO) {
                    userRepo.getCurrentUser()
                }

            }
        }

        LaunchedEffect(true) {
            updateData()
        }

        // callback for when a step is done
        val stepDone: () -> Unit = {
            Log.d("Settings", "done")
            foldViewModel.foldAll()
            updateData()
        }

        val userName = if (currentUser.value == null) stringResource(id = R.string.nologon)
        else currentUser.value!!.full_name
        val scaffoldState = rememberScaffoldState()
        val scope = rememberCoroutineScope()
        val name = stringResource(id = R.string.settings)+": "+userName
        Scaffold(
            topBar = { GenericTopAppBar(title = name,onNavigationIconClick = {
                scope.launch {
                    scaffoldState.drawerState.open()
                }
            }) }
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
                        headlineText = { Text(text = stringResource(R.string.sync_data)) },
                        leadingContent = { Icon(Icons.Filled.Refresh, contentDescription = null) },
                        foldedContent = { SyncBtn(loading = syncInProgress, user = currentUser) },
                    )
                }
                Row {
                    FoldableListItem(
                        1,
                        foldViewModel,
                        foldStates[1],
                        headlineText = { Text(text = stringResource(id = R.string.importfrom)) },
                        leadingContent = { Icon(Icons.Filled.AddCircle, contentDescription = null) },
                        foldedContent = { StuLinkImport(stepDone) },
                    )
                }
                Row {
                    FoldableListItem(
                        2,
                        foldViewModel,
                        foldStates[2],
                        headlineText = { Text(text = stringResource(id = R.string.edit_user_info)) },
                        leadingContent = { Icon(Icons.Filled.Edit, contentDescription = null) },
                        foldedContent = { UserInfoEdit(context = ctx, done = stepDone) },
                    )
                }
                Row {
                    FoldableListItem(
                        3,
                        foldViewModel,
                        foldStates[3],
                        headlineText = { Text(text = stringResource(R.string.language)) },
                        leadingContent = {Icon(Icons.Filled.Settings, contentDescription = null) },
                        foldedContent = { changes(languages, LocalConfiguration.current
                            ,LocalContext.current)
                        }
                    )
                }
                val headText = if (currentUser.value == null) stringResource(id = R.string.register_login)
                else stringResource(id = R.string.logout)
                Row {
                    FoldableListItem(
                        4,
                        foldViewModel,
                        foldStates[4],
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
    CustomButton(onClick = {
        //clear current user session
        coroutineScope.launch {
            Log.d("SettingActivity", "User Logout")
            userRepo.logout()
            user.value = null
            // Navigate back to the setup activity
            val intent = Intent(ctx, SetupActivity::class.java)
            ctx.startActivity(intent)
        }
    }, text = stringResource(id = R.string.logout),enabled = !loading.value && user.value != null)
}

@Composable
fun changes(languages:List<String>,LocalConfiguration:Configuration,LocalContext: Context){
    var selectedLanguage by remember { mutableStateOf(LocalContext.resources.configuration.locales[0].displayName) }
    languages.forEach { language ->
        // Radio button for each language option
        Row{
            RadioButton(
                selected = language.equals(selectedLanguage),
                onClick = {
                    selectedLanguage = language
                    val lan = when(language){
                        "English" -> "en"
                        "简体中文" -> "zh"
                        "Español" -> "es"
                        else -> "en"
                    }
                    // Get the current configuration
                    val configuration = LocalConfiguration

                    // Create a new Locale object with the user-selected language
                    val newLocale = Locale(lan)

                    // Set the new locale in the configuration object
                    configuration.setLocale(newLocale)

                    // Update the configuration with the new locale
                    LocalConfiguration.setTo(configuration)

                    // Get the current context
                    val context = LocalContext

                    // Update the context with the new configuration
                    context.resources.updateConfiguration(configuration, context.resources.displayMetrics)

                }
            )
            CustomText(text = language)
        }

    }
}


@Composable
fun SyncBtn(loading: MutableState<Boolean>, user: MutableState<User?>) {
    val coroutineScope = rememberCoroutineScope()
    val ctx = LocalContext.current
    CustomButton(        onClick = {
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
    }, text = stringResource(id = R.string.sync),enabled = !loading.value && user.value != null)
}