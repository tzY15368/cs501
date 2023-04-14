package com.cs501.cs501app.buotg.view.user_group

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.cs501.cs501app.buotg.database.entities.Group
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.*


class StudyGroupActivity: AppCompatActivity() {

    val groupRepo = AppRepository.get().groupRepo()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ShowView()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ShowView() {
        val coroutineScope = rememberCoroutineScope()
        val ctx = LocalContext.current
        var newGroupName by remember { mutableStateOf("") }
        var newGroupID by remember { mutableStateOf("") }
        var creatingGroup by remember { mutableStateOf(false) }
        var joiningGroup by remember { mutableStateOf(false) }
        var leavingGroup by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {

            Button(onClick = { creatingGroup = true }) {
                Text(text = "Create group")
            }
            Button(onClick = { joiningGroup = true }) {
                Text(text = "Join group")
            }
            Button(onClick = { leavingGroup = true }) {
                Text(text = "Leave group")
            }
        }
        //create study group
        if (creatingGroup) {
            Dialog(onDismissRequest = { creatingGroup = false }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(text = "Create a study group")
                    TextField(
                        value = newGroupName,
                        onValueChange = { newGroupName = it },
                        label = { Text(text = "Group name") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )
                    Button(onClick = {
                        coroutineScope.launch {
                            groupRepo.createGroup(ctx, newGroupName)
                        }
                        creatingGroup = false
                    }) {
                        Text(text = "Create")
                    }
                }
            }
        }
        //join study group
        if (joiningGroup) {
            Dialog(onDismissRequest = { joiningGroup = false }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(text = "Join a study group")
                    TextField(
                        value = newGroupName,
                        onValueChange = { newGroupName = it },
                        label = { Text(text = "Group name") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )
                    Button(onClick = {
                        joiningGroup = false
                    }) {
                        Text(text = "Join")
                    }
                }
            }
        }
        //leave study group
        if (leavingGroup) {
            Dialog(onDismissRequest = { leavingGroup = false }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(text = "Leave a study group")
                    TextField(
                        value = newGroupName,
                        onValueChange = { newGroupName = it },
                        label = { Text(text = "Group name") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )
                    Button(onClick = {
                        leavingGroup = false
                    }) {
                        Text(text = "Leave")
                    }
                }
            }
        }
    }
}



@Preview
@Composable
fun StudyGroupActivityPreview() {
    StudyGroupActivity()
}
