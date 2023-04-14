package com.cs501.cs501app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cs501.cs501app.assignment2.Calc1Activity
import com.cs501.cs501app.assignment2.Calc2Activity
import com.cs501.cs501app.assignment3.flashcard.FCLoginActivity
import com.cs501.cs501app.assignment3.geoquiz.GeoQuizActivity
import com.cs501.cs501app.assignment3.tempconv.TempConvActivity
import com.cs501.cs501app.assignment4.boggle.BoggleActivity
import com.cs501.cs501app.assignment4.cintent.CriminalIntentActivity
import com.cs501.cs501app.assignment4.hangman.HangManActivity
import com.cs501.cs501app.buotg.HomeActivity
import com.cs501.cs501app.buotg.database.AppDatabase
import com.cs501.cs501app.buotg.database.entities.KVEntry
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import com.cs501.cs501app.buotg.view.user_setting.SettingActivity
import com.cs501.cs501app.buotg.view.user_setup.SetupActivity
import com.cs501.cs501app.buotg.view.user_group.StudyGroupActivity
import com.cs501.cs501app.example.WebViewDemo
import com.cs501.cs501app.utils.GenericTopAppBar
import com.cs501.cs501app.utils.TAlert
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val activities = listOf(
        HomeActivity::class,
        SetupActivity::class,
        SettingActivity::class,
        StudyGroupActivity::class,
//        Calc1Activity::class,
//        Calc2Activity::class,
//        GeoQuizActivity::class,
//        TempConvActivity::class,
//        FCLoginActivity::class,
//        CriminalIntentActivity::class,
//        HangManActivity::class,
//        BoggleActivity::class,
    )

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
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
                            activities.forEachIndexed { idx, _ ->
                                ActivityEntry(idx)
                            }
                            Divider()
                            Ping()
                            Divider()
                            KVInterface()
                        }
                    }
                )
            }
        }
    }

    @Composable
    fun ActivityEntry(activityIdx: Int) {
        val activity = activities[activityIdx]
        val simpleName = activity.java.simpleName.replace("Activity", "")
        Button(
            onClick = {
                val intent = Intent(this, activity.java)
                startActivity(intent)
            }
        ) {
            Text(text = "Goto $simpleName")
        }
        Spacer(modifier = Modifier.padding(8.dp))
    }

    @Composable
    fun Ping(){
        val coroutineScope = rememberCoroutineScope()
        val ctx = LocalContext.current
        Button(onClick = {
            coroutineScope.launch {
                val response = AppRepository.get().ping(ctx)
                Log.d("Ping", "response: $response")
                //response?.let { TAlert.success(this@MainActivity, "Ping:${response.message}") }
            }
        }) {
            Text("Ping")
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun KVInterface() {
        val (key, setKey) = remember { mutableStateOf("") }
        val (value, setValue) = remember { mutableStateOf("") }
        val kvDao = AppRepository.get().kvDao()
        val coroutineScope = rememberCoroutineScope()
        Column {
            TextField(
                value = key,
                onValueChange = { setKey(it) },
                label = { Text("Key") }
            )
            TextField(
                value = value,
                onValueChange = { setValue(it) },
                label = { Text("Value") }
            )
            // row of button: put, get ,delete
            Row {
                Button(onClick = {
                    coroutineScope.launch {
                        val kve = KVEntry(key, value)
                        kvDao.put(kve)
                        Log.d("KVInterface", "put:$kve")
                    }
                }) {
                    Text("Put")
                }
                Button(onClick = {
                    coroutineScope.launch {
                        val kve = kvDao.get(key)
                        Log.d("KVInterface", "get: $kve")
                    }
                }) {
                    Text("Get")
                }
                Button(onClick = {
                    coroutineScope.launch {
                        kvDao.delete(key)
                        Log.d("KVInterface", "delete: $key")
                    }
                }) {
                    Text("Delete")
                }
            }
        }
    }
}