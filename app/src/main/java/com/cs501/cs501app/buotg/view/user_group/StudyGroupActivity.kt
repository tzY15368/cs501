package com.cs501.cs501app.buotg.view.user_group

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.ExitToApp

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cs501.cs501app.buotg.connection.API
import com.cs501.cs501app.buotg.database.entities.Group
import com.cs501.cs501app.buotg.database.entities.GroupInvite
import com.cs501.cs501app.buotg.database.entities.User
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import com.cs501.cs501app.buotg.view.common.UserCardView
import com.cs501.cs501app.buotg.view.user_invite.InviteRow
import com.cs501.cs501app.utils.GenericTopAppBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun LeaveGroupBtn(groupID: Int, userId: UUID, callback: suspend () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val groupRepo = AppRepository.get().groupRepo()
    val ctx = LocalContext.current
    OutlinedButton(modifier = Modifier.size(30.dp),
        onClick = {
            coroutineScope.launch {
                groupRepo.leaveGroup(
                    ctx,
                    groupID,
                    userId
                )
                callback()
            }
        }) {
        // use the Icons.Outlined.Delete
        Icon(
            Icons.Outlined.ExitToApp,
            contentDescription = "Delete",
            modifier = Modifier.size(20.dp)
        )
    }
}

class StudyGroupActivity : AppCompatActivity() {

    val groupRepo = AppRepository.get().groupRepo()
    val userRepo = AppRepository.get().userRepo()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                GroupNavHost()
            }
        }
    }

    @Composable
    fun GroupNavHost(

    ) {
        val navController = rememberNavController()
        val startDestination = "listView"
        NavHost(navController = navController, startDestination = startDestination) {
            composable("listView") {
                ShowView(onNavigateToGroupDetails = {
                    navController.navigate("groupDetails/$it")
                })
            }
            composable("groupDetails/{groupID}") { backStackEntry ->
                println(backStackEntry.arguments)
                GroupDetails(Integer.parseInt(backStackEntry.arguments?.getString("groupID")))
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun GroupInviteView(groupID: Int) {
        val inviteRepo = AppRepository.get().inviteRepository()
        var userEmail by remember { mutableStateOf("") }
        val loading = remember { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()
        val ctx = LocalContext.current

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            TextField(
                value = userEmail,
                onValueChange = {
                    userEmail = it
                },
                label = { Text("User Email") },
                placeholder = { Text("Enter User Email") },
                leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
                //add function for trailing icon
                singleLine = true,
            )
            Button(onClick = {
                coroutineScope.launch {
                    loading.value = true
                    inviteRepo.upsertInvite(ctx, groupID, userEmail, API.InviteStatus.PENDING)
                    loading.value = false
                }
            }, enabled = !loading.value) {
                Text("Send Invite")
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun GroupDetails(groupID: Int) {
        var group by remember { mutableStateOf<Group?>(null) }
        var groupOwner by remember { mutableStateOf<User?>(null) }
        var groupMmebers by remember { mutableStateOf(listOf<User>()) }
        var currentUser by remember { mutableStateOf<User?>(null) }
        var invites by remember { mutableStateOf(listOf<GroupInvite>()) }
        val inviteRepo = AppRepository.get().inviteRepository()
        val ctx = LocalContext.current

        suspend fun loadGroup() {
            groupRepo.getGroup(ctx, groupID)?.let {
                group = it.group
                groupOwner = userRepo.fetchUser(ctx, it.group.owner_id)
                currentUser = userRepo.getCurrentUser()
                if(group==null){
                    return
                }

                // pull group invites
                val newInvites = mutableListOf<GroupInvite>()
                inviteRepo.listGroupInvites(ctx, group!!.group_id)?.let { ivts ->
                    newInvites.addAll(ivts.group_invites)
                }
                invites = newInvites
                Log.d("GroupDetails", "Invites: ${invites.size}")
            }
            groupRepo.getGroupMembers(ctx, groupID)?.let {
                val uuids = it.group_members
                val users = mutableListOf<User>()
                for (uuid in uuids) {
                    userRepo.fetchUser(ctx, uuid)?.let { user ->
                        users.add(user)
                    }
                }
                groupMmebers = users
            }
        }


        LaunchedEffect(true) {
            loadGroup()
        }
        Scaffold(
            topBar = {
                GenericTopAppBar(title = "Study Groups: ${group?.group_name}")
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Text("Group Info:", fontSize = 25.sp)
                group?.let {
                    Text(text = "Group Name: ${it.group_name}", fontSize = 20.sp)
                    Text(text = "Group Description: ${it.desc}", fontSize = 20.sp)
                    Text(text = "Group Created By: ${groupOwner?.full_name}", fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Group Members:", fontSize = 20.sp)
                LazyColumn(content = {
                    items(groupMmebers.size) { index ->
                        Row {
                            UserCardView(User = groupMmebers[index])
                            currentUser?.let { currentU ->
                                if (currentU.user_id == groupOwner?.user_id) {
                                    LeaveGroupBtn(
                                        groupID,
                                        groupMmebers[index].user_id,
                                        callback = ::loadGroup
                                    )
                                }
                            }
                        }
                    }
                })
                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Group Invites:", fontSize = 20.sp)
                LazyColumn(content = {
                    items(invites.size) { index ->
                        InviteRow(
                            groupInvite = invites[index],
                            reload = ::nothing,
                            allowModify = false
                        )
                    }
                })
                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Spacer(modifier = Modifier.height(16.dp))
                if (group != null) {
                    GroupInviteView(group!!.group_id)
                }
            }
        }
    }

    suspend fun nothing(){}

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ShowView(onNavigateToGroupDetails: (Int) -> Unit) {
        val coroutineScope = rememberCoroutineScope()
        val ctx = LocalContext.current
        var newGroupName by remember { mutableStateOf("") }
        var creatingGroup by remember { mutableStateOf(false) }
        var joiningGroup by remember { mutableStateOf(false) }
        var newGroupDesc by remember { mutableStateOf("") }
        var groups by remember { mutableStateOf(listOf<Group>()) }
        var currentUser by remember { mutableStateOf<User?>(null) }

        suspend fun reloadGroups() {
            val resp = groupRepo.listGroups(ctx)
            if (resp != null) {
                groups = resp.groups
            }
        }

        @Composable
        fun GroupView(group: Group) {
            var createdbyUser by remember { mutableStateOf<User?>(null) }
            LaunchedEffect(true) {
                currentUser = userRepo.getCurrentUser()
                userRepo.fetchUser(ctx, group.owner_id)?.let {
                    createdbyUser = it
                }

            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                val by = if (createdbyUser != null) " by " + createdbyUser!!.full_name else ""
                Text(
                    text = group.group_name + by,
                    fontSize = 30.sp,
                    modifier = Modifier.clickable {
                        onNavigateToGroupDetails(group.group_id)
                    })
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = group.desc, fontSize = 15.sp)
                    currentUser?.let {
                        LeaveGroupBtn(
                            groupID = group.group_id,
                            userId = it.user_id,
                            callback = {
                                reloadGroups()
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Divider()
            }
        }

        @Composable
        fun GroupList() {
            val ctx = LocalContext.current
            LaunchedEffect(true) {
                reloadGroups()
            }
            if (groups.isEmpty()) {
                Text(text = "No groups found")
            }
            Column {
                for (group in groups) {
                    GroupView(group = group)
                }
            }
        }

        Scaffold(
            topBar = {
                GenericTopAppBar(title = "Study Groups")
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {

                GroupList()

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Row() {
                        Button(onClick = { creatingGroup = true }) {
                            Text(text = "Create group")
                        }
                        Button(onClick = { joiningGroup = true }) {
                            Text(text = "Join group")
                        }
                    }
                }


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
                    TextField(
                        value = newGroupDesc,
                        onValueChange = { newGroupDesc = it },
                        label = { Text(text = "Group description") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )
                    Button(onClick = {
                        coroutineScope.launch {
                            groupRepo.createGroup(ctx, newGroupName, newGroupDesc)
                            reloadGroups()
                        }
                        creatingGroup = false
                    }) {
                        Text(text = "Create")
                    }
                }
            }
        }
        //join study group
//        if (joiningGroup) {
//            Dialog(onDismissRequest = { joiningGroup = false }) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .fillMaxHeight()
//                        .padding(16.dp)
//                        .verticalScroll(rememberScrollState()),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center,
//                ) {
//                    Text(text = "Join a study group")
//                    TextField(
//                        value = newGroupName,
//                        onValueChange = { newGroupName = it },
//                        label = { Text(text = "Group name") },
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
//                    )
//                    Button(onClick = {
//                        coroutineScope.launch {
//                            joiningGroup = false
//                            reloadGroups()
//                        }
//                    }) {
//                        Text(text = "Join")
//                    }
//                }
//            }
//        }
    }
}


@Preview
@Composable
fun StudyGroupActivityPreview() {
    StudyGroupActivity()
}
