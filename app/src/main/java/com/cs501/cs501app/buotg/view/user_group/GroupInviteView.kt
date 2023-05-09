package com.cs501.cs501app.buotg.view.user_group

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cs501.cs501app.R
import com.cs501.cs501app.buotg.CustomButton
import com.cs501.cs501app.buotg.CustomTextField
import com.cs501.cs501app.buotg.connection.API
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import kotlinx.coroutines.launch

@Composable
fun GroupInviteView(groupID: Int) {
    val inviteRepo = AppRepository.get().inviteRepository()
    var userEmail by remember { mutableStateOf("") }
    val loading = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val ctx = LocalContext.current

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        CustomTextField(
            modifier = Modifier.padding(4.dp),
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
        }, enabled = !loading.value, text = stringResource(id = R.string.send_invites),modifier = Modifier.padding(4.dp),)
    }
}