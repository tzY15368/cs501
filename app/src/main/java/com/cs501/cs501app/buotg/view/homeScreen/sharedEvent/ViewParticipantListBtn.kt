package com.cs501.cs501app.buotg.view.homeScreen.sharedEvent

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cs501.cs501app.buotg.database.entities.User
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import com.cs501.cs501app.buotg.view.dayNightTheme.RedPrimary
import com.cs501.cs501app.buotg.view.dayNightTheme.RedPrimaryVariant

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
        Text(text = participanceInfo, color = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))
        Divider(color = RedPrimaryVariant)
    }

    Column(
        modifier = Modifier.fillMaxHeight(0.5f)
            .background(Color.White)
            .padding(16.dp)
    ) {
        LaunchedEffect(true) {
            loadParticipants()
        }
        if (participantInfo.isEmpty()) {
            Text(
                text = "No participants yet",
                color = Color.Black,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(participantInfo) { participanceInfo ->
                    ParticipantView(participanceInfo)
                }
            }
        }
    }

}