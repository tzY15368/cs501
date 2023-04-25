package com.cs501.cs501app.buotg.view.user_invite

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cs501.cs501app.buotg.connection.API
import com.cs501.cs501app.buotg.database.entities.Group
import com.cs501.cs501app.buotg.database.entities.GroupInvite
import com.cs501.cs501app.buotg.database.entities.User
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import kotlinx.coroutines.launch
import com.cs501.cs501app.R

@Composable
fun InviteRow(groupInvite: GroupInvite, reload: suspend () -> Unit, allowModify:Boolean) {
    var group by remember { mutableStateOf<Group?>(null) }
    var invitedBy by remember { mutableStateOf<User?>(null) }
    val ctx = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(true) {
        group = AppRepository.get().groupRepo().getGroup(ctx, groupInvite.group_id)?.group
        group?.let {
            invitedBy = AppRepository.get().userRepo().fetchUser(ctx, it.owner_id)
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Person,
            contentDescription = "Person Icon",
            modifier = Modifier
                .padding(8.dp)
                .size(32.dp)
        )
        val Group = stringResource(id = R.string.group)
        val Invited_by = stringResource(id = R.string.invited_by)
        Text(text = "$Group : ${group?.group_name}")
        Text(text = "$Invited_by : ${invitedBy?.full_name}")
        if(allowModify){
            if (groupInvite.status != API.InviteStatus.SUCCESS) {
                Button(onClick = {
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
                }) {
                    Text(text = stringResource(id = R.string.accept))
                }
            }
            if (groupInvite.status != API.InviteStatus.FAIL) {
                Button(onClick = {
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
                }) {
                    Text(text = stringResource(id = R.string.reject))
                }
            }
        }
    }
}