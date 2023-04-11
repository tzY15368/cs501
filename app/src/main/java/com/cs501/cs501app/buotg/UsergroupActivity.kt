package com.cs501.cs501app.buotg

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog


data class StudyGroup(val name: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyGroupActivity() {
    var studyGroups by remember { mutableStateOf(emptyList<StudyGroup>()) }
    var newGroupName by remember { mutableStateOf("") }
    var creatingGroup by remember { mutableStateOf(false) }
    var joiningGroup by remember { mutableStateOf(false) }
    var leavingGroup by remember { mutableStateOf(false) }
    //show all study groups
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        studyGroups.forEach { studyGroup ->
            Text(text = studyGroup.name)
        }
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
                    studyGroups = studyGroups + StudyGroup(newGroupName)
                    //add backend code here

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
                    studyGroups = studyGroups + StudyGroup(newGroupName)
                    //add backend code here

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
                    studyGroups = studyGroups.filter { it.name != newGroupName }
                    //add backend code here

                    leavingGroup = false
                }) {
                    Text(text = "Leave")
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
