package com.cs501.cs501app.buotg.view.user_group

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.rememberScaffoldState

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight

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
import com.cs501.cs501app.buotg.view.thirdParty.chatRoom.ChatApplication
import com.cs501.cs501app.buotg.view.user_invite.InviteRow
import com.cs501.cs501app.utils.GenericTopAppBar
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
        val targetApp = (application as ChatApplication)
        val chatClient = (application as ChatApplication).client
        val name = (application as ChatApplication).userName
        setContent {
            MaterialTheme {
                GroupNavHost(targetApp)
            }
        }
    }

    @Composable
    fun GroupNavHost(targetApp: ChatApplication) {
        val navController = rememberNavController()
        val startDestination = "listView"
        NavHost(navController = navController, startDestination = startDestination) {
            composable("listView") {
                ShowView(onNavigateToGroupDetails = {
                    navController.navigate("groupDetails/$it")
                }, targetApp)
            }
            composable("groupDetails/{groupID}") { backStackEntry ->
                println(backStackEntry.arguments)
                GroupDetails(Integer.parseInt(backStackEntry.arguments?.getString("groupID")), onBack = {
                    navController.popBackStack()
                })
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ShowView(onNavigateToGroupDetails: (Int) -> Unit, targetApp: ChatApplication) {
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
                    .padding(16.dp)
            ) {
                val creator = stringResource(id = R.string.creator)
                val by =
                    if (createdbyUser != null) " $creator: " + createdbyUser!!.full_name else ""
                Text(
                    text = "(${group.group_id}) "+group.group_name + by,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E2E2E),
                    modifier = Modifier
                        .clickable {
                            onNavigateToGroupDetails(group.group_id)
                        }
                        .padding(bottom = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = group.desc,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF7F7F7F),
                        modifier = Modifier.weight(1f)
                    )
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
                Divider(color = Color(0xFFF52D4D), thickness = 1.dp, modifier = Modifier.padding(vertical = 16.dp))
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
            LazyColumn(content = {
                items(groups.size) { index ->
                    GroupView(groups[index])
                }
            }, modifier = Modifier.height(500.dp))
        }

        @Composable
        fun CreateGroup() {
            Dialog(onDismissRequest = { creatingGroup = false }) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()).background(Color.White),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    CustomText(
                        text = stringResource(id = R.string.create_a_group),
                        style = androidx.compose.material.MaterialTheme.typography.h5,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    CustomTextField(
                        value = newGroupName,
                        onValueChange = { newGroupName = it },
                        label = stringResource(id = R.string.group_name),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    CustomTextField(
                        value = newGroupDesc,
                        onValueChange = { newGroupDesc = it },
                        label = stringResource(id = R.string.group_description),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    CustomButton(
                        onClick = {
                            coroutineScope.launch {
                                groupRepo.createGroup(ctx, newGroupName, newGroupDesc)
                                targetApp.create_channel("$newGroupName" + "_study_group")
                                reloadGroups()
                            }
                            creatingGroup = false
                        },
                        text = stringResource(id = R.string.create),
                        modifier = Modifier.fillMaxHeight(0.5f)
                    )
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
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row{
                    GroupList()
                }
                Row{
                    CustomButton(
                        onClick = { creatingGroup = true },
                        text = stringResource(id = R.string.create_group)
                    )
                }
            }
        }


        //create study group
        if (creatingGroup) {
            CreateGroup()
        }
    }
}


@Preview
@Composable
fun StudyGroupActivityPreview() {
    StudyGroupActivity()
}