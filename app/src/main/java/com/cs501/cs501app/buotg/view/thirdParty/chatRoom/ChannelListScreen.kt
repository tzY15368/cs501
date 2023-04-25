package com.cs501.cs501app.buotg.view.thirdParty.chatRoom

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.ui.avatar.AvatarView
import io.getstream.chat.android.ui.common.extensions.getDisplayName

@Composable
fun ChannelListScreen(
    navController: NavController,
    channelList: MutableState<List<Channel>>,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        LazyColumn(Modifier.fillMaxSize()) {
            items(channelList.value) { channel ->
                ChannelListItem(
                    channel = channel,
                    onClick = { navController.navigate("messagelist/${channel.cid}") },
                )
                Divider()
            }
        }
    }
}

@Composable
fun ChannelListItem(
    channel: Channel,
    onClick: (channel: Channel) -> Unit,
) {
    Row(
        modifier = Modifier
            .clickable { onClick(channel) }
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Avatar(channel)
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(
                text = channel.getDisplayName(LocalContext.current),
                style = TextStyle(fontWeight = FontWeight.Bold),
                fontSize = 18.sp,
            )

            val lastMessageText = channel.messages.lastOrNull()?.text ?: "..."
            Text(
                text = lastMessageText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun Avatar(channel: Channel) {
    AndroidView(
        factory = { context -> AvatarView(context) },
        update = { view -> view.setChannelData(channel) },
        modifier = Modifier.size(48.dp)
    )
}