package com.cs501.cs501app.buotg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import com.cs501.cs501app.buotg.view.dayNightTheme.EventTrackerTheme
import com.cs501.cs501app.buotg.database.entities.User
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

                val currentUser: User? = AppRepository.get().getUser(0)
                Log.d("Home", "got user:$currentUser")
                setUser(currentUser)
            }
        }
        Text(text = user.toString())
    }
}