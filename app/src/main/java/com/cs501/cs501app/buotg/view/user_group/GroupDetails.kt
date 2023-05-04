package com.cs501.cs501app.buotg.view.user_group

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cs501.cs501app.R
import com.cs501.cs501app.buotg.database.entities.Group
import com.cs501.cs501app.buotg.database.entities.GroupInvite
import com.cs501.cs501app.buotg.database.entities.User
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import com.cs501.cs501app.buotg.view.common.UserCardView
import com.cs501.cs501app.buotg.view.user_invite.InviteRow
import com.cs501.cs501app.utils.GenericTopAppBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDetails(groupID: Int, onBack: () -> Unit) {
    val groupRepo = AppRepository.get().groupRepo()
    val userRepo = AppRepository.get().userRepo()
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
                finished = { onBack() })
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