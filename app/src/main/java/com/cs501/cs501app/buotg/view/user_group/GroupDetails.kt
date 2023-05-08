package com.cs501.cs501app.buotg.view.user_group

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
            val groupInfoTitle = stringResource(id = R.string.group_info)
            Text(text = groupInfoTitle, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            group?.let { group ->
                val groupNameLabel = stringResource(id = R.string.group_name)
                val groupDescriptionLabel = stringResource(id = R.string.group_description)
                val groupCreatedLabel = stringResource(id = R.string.group_created)
                Text(text = "$groupNameLabel: ${group.group_name}", fontSize = 18.sp)
                Text(text = "$groupDescriptionLabel: ${group.desc}", fontSize = 18.sp)
                Text(text = "$groupCreatedLabel: ${groupOwner?.full_name}", fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Divider(color = Color(0xFFF52D4D), thickness = 1.dp)
            Spacer(modifier = Modifier.height(24.dp))
            val membersTitle = stringResource(id = R.string.group_member)
            Text(text = membersTitle, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            LazyColumn(
                modifier = Modifier.padding(horizontal = 8.dp),
                content = {
                    items(groupMmebers.size) { index ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .background(
                                    if (index % 2 == 0) Color.LightGray else Color.White
                                )
                                .fillMaxWidth()
                        ) {
                            UserCardView(User = groupMmebers[index])
                            currentUser?.let { currentUser ->
                                if (currentUser.user_id == groupOwner?.user_id) {
                                    LeaveGroupBtn(
                                        groupID = groupID,
                                        userId = groupMmebers[index].user_id,
                                        callback = ::loadGroup,
                                    )
                                }
                            }
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.height(24.dp))
            Divider(color = Color(0xFFF52D4D), thickness = 1.dp)
            Spacer(modifier = Modifier.height(24.dp))
            val invitesTitle = stringResource(id = R.string.group_invites)
            Text(text = invitesTitle, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            LazyColumn(
                modifier = Modifier.padding(horizontal = 8.dp),
                content = {
                    items(invites.size) { index ->
                        InviteRow(
                            groupInvite = invites[index],
                            reload = ::nothing,
                            allowModify = false,
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(24.dp))
            Divider(color = Color(0xFFF52D4D), thickness = 1.dp)
            Spacer(modifier = Modifier.height(24.dp))
            if (group != null) {
                GroupInviteView(group!!.group_id)
            }
        }


    }
}


suspend fun nothing() {}