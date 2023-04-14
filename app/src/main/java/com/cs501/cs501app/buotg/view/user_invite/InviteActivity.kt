package com.cs501.cs501app.buotg.view.user_invite

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

//a list to save user names get from server
val userNames = listOf("user1","user2")

class InviteActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ShowView()
            }
        }
    }
    @OptIn(ExperimentalMaterial3Api::class)
    @Preview
    @Composable
    fun ShowView() {
        Column() {
            TextField(
                value = "",
                onValueChange = {},
                label = { Text("User Name") },
                placeholder = { Text("Enter User Name") },
                leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
                trailingIcon = { Icon(Icons.Filled.Refresh, contentDescription = null) },
                //add function for trailing icon
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            //a list to show user names get from server and button to invite them
            LazyColumn() {
                items(userNames.size) { index ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text(text = userNames[index])
                        Button(onClick = { /*TODO*/ }) {
                            Text(text = "Invite")
                        }
                    }
                }
            }
        }
    }
}