package com.cs501.cs501app.buotg.view.homeScreen.sharedEvent

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cs501.cs501app.R
import com.cs501.cs501app.buotg.CustomTextField
import com.cs501.cs501app.buotg.database.entities.Group
import com.cs501.cs501app.buotg.database.entities.SharedEventParticipance
import com.cs501.cs501app.buotg.database.entities.Status
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import com.cs501.cs501app.buotg.view.dayNightTheme.RedPrimaryVariant
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun importUsersView(sharedEventId: Int, reloadSharedEvents: suspend () -> Unit) {
    var groupIdMenuExpanded by remember { mutableStateOf(false) }
    var selectedGroupId = remember { mutableStateOf(0) }
    var selectedGroupName = remember { mutableStateOf("") }
    val ctx = LocalContext.current
    var groups = remember { mutableStateListOf<Group>() }

    LaunchedEffect(true) {
        val res = AppRepository.get().groupRepo().listGroups(ctx)
        res?.let {
            groups.addAll(it.groups)
        }
        Log.d("ImportGroupBtn", "groups: ${groups.size}")
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(id = R.string.import_members_2),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            ExposedDropdownMenuBox(
                expanded = groupIdMenuExpanded,
                onExpandedChange = { groupIdMenuExpanded = !groupIdMenuExpanded }) {
                CustomTextField(
                    value = "(${selectedGroupId.value}): ${selectedGroupName.value}",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = groupIdMenuExpanded) },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(expanded = groupIdMenuExpanded, onDismissRequest = {
                    groupIdMenuExpanded = false
                }) {
                    groups.map {
                        DropdownMenuItem(
                            onClick = {
                                selectedGroupId.value = it.group_id
                                selectedGroupName.value = it.group_name
                                groupIdMenuExpanded = false
                            },
                            text = { Text(text = "(${it.group_id})${it.group_name}") }
                        )
                        Divider(color = RedPrimaryVariant)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        importUsersBtn(
            sharedEventId = sharedEventId,
            groupId = selectedGroupId,
            callback = { reloadSharedEvents() },
            enabled = selectedGroupId.value != 0,
        )
    }

}

@Composable
fun importUsersBtn(
    sharedEventId: Int,
    groupId: MutableState<Int>,
    callback: suspend () -> Unit,
    enabled: Boolean
) {
    val coroutineScope = rememberCoroutineScope()
    val sharedEventParticipanceRepo = AppRepository.get().sharedEventParticipanceRepo()
    val groupRepo = AppRepository.get().groupRepo()
    val ctx = LocalContext.current
    Log.d("ImportGroupBtn", "groupId: ${groupId.value}, enabled: ${enabled}")
    OutlinedButton(modifier = Modifier.size(60.dp),
        //enabled = enabled,
        onClick = {
            Log.d("CLICKED_IMPORT", groupId.toString())
            coroutineScope.launch {
                Log.d("group id", groupId.toString())
                var groupMembers = groupRepo.getGroupMembers(ctx, groupId.value)
                val uuids = groupMembers?.group_members
                Log.d("gm list", uuids.toString())
                if (uuids != null) {
                    for (gm in uuids) {
                        Log.d("gm ", gm.toString())
                        val participance = SharedEventParticipance(
                            shared_event_id = sharedEventId,
                            user_id = gm,
                            status = Status.FAIL
                        )
                        sharedEventParticipanceRepo.putParticipance(participance, ctx)
                    }
                }
                callback()
            }

        }) {
        Icon(
            Icons.Outlined.Add,
            contentDescription = stringResource(id = R.string.import_members),
            modifier = Modifier.size(20.dp)
        )

    }
}