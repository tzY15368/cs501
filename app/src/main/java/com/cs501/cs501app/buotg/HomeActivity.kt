package com.cs501.cs501app.buotg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cs501.cs501app.buotg.database.AppRepository
import com.cs501.cs501app.buotg.dayNightTheme.EventTrackerTheme
import com.cs501.cs501app.buotg.entities.Event
import com.cs501.cs501app.buotg.entities.User
import com.cs501.cs501app.utils.GenericTopAppBar
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            EventTrackerTheme {
                EventTracker()
            }
        }
//        setContent {
//            MaterialTheme {
//                Scaffold(
//                    topBar = {
//                        GenericTopAppBar()
//                    },
//                    content = { innerPadding ->
//                        // center the column
//                        Column(
//                            modifier = Modifier
//                                .padding(innerPadding)
//                                .padding(16.dp)
//                                .fillMaxWidth()
//                                .fillMaxHeight()
//                                .verticalScroll(rememberScrollState()),
//                            horizontalAlignment = Alignment.CenterHorizontally,
//                            verticalArrangement = Arrangement.Center,
//                        ) {
//                            ShowUser()
//                        }
//                    }
//                )
//            }
//        }
    }

    @Composable
    fun ShowUser(){
        val coroutineScope = rememberCoroutineScope()
        val (user, setUser) = remember{ mutableStateOf<User?>(null) }
        LaunchedEffect(true){
            coroutineScope.launch {

                val currentUser:User? = AppRepository.get().getUser(0)
                Log.d("Home", "got user:$currentUser")
                setUser(currentUser)
            }
        }
        Text(text = user.toString())
    }
}