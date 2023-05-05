package com.cs501.cs501app.buotg.view.homeScreen.sharedEvent

import android.util.Log
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cs501.cs501app.R
import com.cs501.cs501app.buotg.database.entities.SharedEventParticipance
import com.cs501.cs501app.buotg.database.entities.Status
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import kotlinx.coroutines.launch

@Composable
fun importUsersBtn(sharedEventId: Int, groupId: Int, callback: suspend () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val sharedEventParticipanceRepo = AppRepository.get().sharedEventParticipanceRepo()
    val groupRepo = AppRepository.get().groupRepo()
    val ctx = LocalContext.current
    OutlinedButton(modifier = Modifier.size(30.dp),
        onClick = {
            Log.d("CLICKED_IMPORT", groupId.toString())
            coroutineScope.launch {
                Log.d("group id", groupId.toString())
                var groupMembers = groupRepo.getGroupMembers(ctx,groupId)
                val uuids = groupMembers?.group_members
                Log.d("gm list", groupMembers.toString())
                if (groupMembers != null) {
                    if (uuids != null) {
                        for(gm in uuids) {
                            Log.d("gm ", gm.toString())
                            val participance = SharedEventParticipance(shared_event_id = sharedEventId, user_id = gm, status = Status.FAIL)
                            sharedEventParticipanceRepo.updateParticipance(participance,ctx)
                        }
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