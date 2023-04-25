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
import androidx.compose.material.rememberScaffoldState

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource

import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cs501.cs501app.R
import com.cs501.cs501app.buotg.CustomButton
import com.cs501.cs501app.buotg.CustomText
import com.cs501.cs501app.buotg.CustomTextField
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
            contentDescription = stringResource(id = R.string.delete),
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
            CustomTextField(
                value = userEmail,
                onValueChange = {
                    userEmail = it
                },
                label = stringResource(id = R.string.user_email),
                placeholder = { Text(stringResource(id = R.string.enter_user_email)) },
                leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) })
            CustomButton(onClick = {
                coroutineScope.launch {
                    loading.value = true
                    inviteRepo.upsertInvite(ctx, groupID, userEmail, API.InviteStatus.PENDING)
                    loading.value = false
                }
            }, enabled = !loading.value, text = stringResource(id = R.string.send_invites))
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
                if (group == null) {
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
        val scaffoldState = rememberScaffoldState()
        val scope = rememberCoroutineScope()

        Scaffold(
            topBar = {
                val study_group = stringResource(id = R.string.study_group)
                GenericTopAppBar(
                    title = "$study_group: ${group?.group_name}",
                    onNavigationIconClick = {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    },
                    finished = { finish() })
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                val group_info = stringResource(id = R.string.group_info)
                Text("$group_info:", fontSize = 25.sp)
                group?.let {
                    val group_name = stringResource(id = R.string.group_name)
                    val group_des = stringResource(id = R.string.group_description)
                    val group_created = stringResource(id = R.string.group_created)
                    Text(text = "$group_name: ${it.group_name}", fontSize = 20.sp)
                    Text(text = "$group_des: ${it.desc}", fontSize = 20.sp)
                    Text(text = "$group_created: ${groupOwner?.full_name}", fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Spacer(modifier = Modifier.height(16.dp))
                val members = stringResource(id = R.string.group_member)
                Text(text = "$members:", fontSize = 20.sp)
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
                val group_invites = stringResource(id = R.string.group_invites)
                Text(text = "$group_invites:", fontSize = 20.sp)
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

    suspend fun nothing() {}

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
                //@TODO Is this expression fluent?
                val creator = stringResource(id = R.string.creator)
                val by =
                    if (createdbyUser != null) " $creator: " + createdbyUser!!.full_name else ""
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
                Text(text = stringResource(id = R.string.no_group_found))
            }
            Column {
                for (group in groups) {
                    GroupView(group = group)
                }
            }
        }

        val scaffoldState = rememberScaffoldState()
        val scope = rememberCoroutineScope()
        Scaffold(
            topBar = {
                GenericTopAppBar(
                    title = stringResource(id = R.string.study_group),
                    onNavigationIconClick = {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    },
                    finished = { finish() })
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
                        CustomButton(
                            onClick = { creatingGroup = true },
                            text = stringResource(id = R.string.create_group)
                        )
                        CustomButton(
                            onClick = { joiningGroup = true },
                            text = stringResource(id = R.string.join_group)
                        )
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
                    CustomText(text = stringResource(id = R.string.create_a_group))
                    CustomTextField(
                        value = newGroupName,
                        onValueChange = { newGroupName = it },
                        label = stringResource(id = R.string.group_name),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )
                    CustomTextField(
                        value = newGroupDesc,
                        onValueChange = { newGroupDesc = it },
                        label = stringResource(id = R.string.group_description),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )
                    CustomButton(onClick = {
                        coroutineScope.launch {
                            groupRepo.createGroup(ctx, newGroupName, newGroupDesc)
                            reloadGroups()
                        }
                        creatingGroup = false
                    }, text = stringResource(id = R.string.create))
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
