package com.cs501.cs501app.buotg.view.homeScreen.sharedEvent

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.cs501.cs501app.buotg.database.entities.User
import com.cs501.cs501app.buotg.database.repositories.AppRepository

@Composable
fun viewParticipantListBtn(sharedEventId: Int) {

    val sharedEventParticipanceRepo = AppRepository.get().sharedEventParticipanceRepo()
    //var participants by remember { mutableStateOf(listOf<User>()) }
    var participantInfo by remember { mutableStateOf(mutableListOf<String>()) }
    val ctx = LocalContext.current
    suspend fun loadParticipants() {
        Log.d("loadParticipants",sharedEventId.toString())
        val result = sharedEventParticipanceRepo.getSharedEventParticipanceBySharedEventId(ctx, sharedEventId)
        val infoList: List<String> = result.map { it.user.full_name + " " + it.shared_event_participance.status.toString() }
//        Log.d("loadParticipants",participants.toString())
//        for(p in participants) {
//            val status = sharedEventParticipanceRepo.getSharedEventParticipanceStatus(sharedEventId, p.user_id).status
//            participantInfo.add(p.full_name + " " + status.toString())
//        }
        participantInfo = infoList.toMutableList()
    }

    @Composable
    fun ParticipantView(participanceInfo: String) {
        Text(text = participanceInfo,color = Color.Black)
        Spacer(modifier = Modifier.height(10.dp))
        Divider()
    }


    LaunchedEffect(true) {
        loadParticipants()
    }
    if(participantInfo.isEmpty()) {
        Text(text ="No participants yet", color = Color.Black)
    }
    LazyColumn(modifier = Modifier.fillMaxHeight().background(color = Color.White), content = {
        items(participantInfo.size) { idx ->
            ParticipantView(participantInfo[idx])
        }

    })
}