package com.cs501.cs501app

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.cs501.cs501app.buotg.BUOTGApplication
import com.cs501.cs501app.buotg.view.homeScreen.HomeActivity
import com.cs501.cs501app.utils.*
import com.cs501.cs501app.utils.GenericTopAppBar
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val scaffoldState = rememberScaffoldState()
                val scope = rememberCoroutineScope()
                (application as BUOTGApplication).ConnectAndShowChatApp()
                Scaffold(
                    scaffoldState = scaffoldState,
                    topBar = {
                        GenericTopAppBar(
                            onNavigationIconClick = {
                                scope.launch {
                                    scaffoldState.drawerState.open()
                                }
                            },
                            hasNavMenu = true
                        )
                    },
                    drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
                    drawerContent = {
                        Row() {

                        }

                        Row() {
                            ActivityEntry(activityIdx = 0)
                        }
                    }
                ) {

                }
            }
        }
    }


    @Composable
    fun ActivityEntry(activityIdx: Int) {
        Button(
            onClick = {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
        ) {
            Text(text = "Goto home")
        }
        Spacer(modifier = Modifier.padding(8.dp))
    }

}