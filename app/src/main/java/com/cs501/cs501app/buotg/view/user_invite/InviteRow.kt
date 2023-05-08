package com.cs501.cs501app.buotg.view.user_invite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import com.cs501.cs501app.buotg.connection.API
import com.cs501.cs501app.buotg.database.entities.Group
import com.cs501.cs501app.buotg.database.entities.GroupInvite
import com.cs501.cs501app.buotg.database.entities.User
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import kotlinx.coroutines.launch
import com.cs501.cs501app.R
import com.cs501.cs501app.buotg.CustomButton

@Composable
fun InviteRow(
    groupInvite: GroupInvite,
    reload: suspend () -> Unit,
    allowModify: Boolean
) {
    var group by remember { mutableStateOf<Group?>(null) }
    var invitedBy by remember { mutableStateOf<User?>(null) }
    val ctx = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Fetch group and inviter information from API
    LaunchedEffect(true) {
        group = AppRepository.get().groupRepo().getGroup(ctx, groupInvite.group_id)?.group
        group?.let {
            invitedBy = AppRepository.get().userRepo().fetchUser(ctx, it.owner_id)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(end = 16.dp)
                .size(48.dp)
                .background(Color.LightGray, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Person Icon",
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.Center)
            )
        }
        Column(
            modifier = Modifier.weight(1f)
        ) {
            val groupText = stringResource(id = R.string.group)
            val invitedByText = stringResource(id = R.string.invited_by)
            Text(
                text = "$groupText: ${group?.group_name}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = "$invitedByText: ${invitedBy?.full_name}",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
        if (allowModify) {
            if (groupInvite.status != API.InviteStatus.SUCCESS) {
                CustomButton(
                    onClick = {
                        coroutineScope.launch {
                            val currentUser = AppRepository.get().userRepo().getCurrentUser()
                            AppRepository.get().inviteRepository().upsertInvite(
                                ctx,
                                groupInvite.group_id,
                                currentUser?.email!!,
                                API.InviteStatus.SUCCESS
                            )
                            reload()
                        }
                    },
                    text = stringResource(id = R.string.accept),
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
            if (groupInvite.status != API.InviteStatus.FAIL) {
                CustomButton(
                    onClick = {
                        coroutineScope.launch {
                            val currentUser = AppRepository.get().userRepo().getCurrentUser()
                            AppRepository.get().inviteRepository().upsertInvite(
                                ctx,
                                groupInvite.group_id,
                                currentUser?.email!!,
                                API.InviteStatus.FAIL
                            )
                            reload()
                        }
                    },
                    text = stringResource(id = R.string.reject)
                )
            }
        }
    }
}
