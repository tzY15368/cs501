package com.cs501.cs501app.buotg.view.thirdParty.chatRoom

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cs501.cs501app.buotg.BUOTGApplication
import com.cs501.cs501app.buotg.ClientProvider
import com.cs501.cs501app.buotg.view.dayNightTheme.ComposeChatTheme
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.QueryChannelsRequest
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.models.Filters

class ChatRoomActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val chatClient = (application as ChatApplication).client
        setContent {
            ComposeChatTheme {
                Surface(color = MaterialTheme.colors.background) {
                    ChatApp(chatClient)
                }
            }
        }
    }
}

@Composable
fun ChatApp(client: ChatClient) {
    Log.d("ChatApp", "ChatApp: ")
    val request = QueryChannelsRequest(
        filter = Filters.and(
            Filters.eq("type", "messaging")
        ),
        offset = 0,
        limit = 10
    ).apply {
        watch = true
        state = true
    }

    val channelList = remember { mutableStateOf(listOf<Channel>()) }
    client.queryChannels(request).enqueue { result ->
        if (result.isSuccess) {
            println("CHECK: " + result.data())
            channelList.value = result.data()

        } else {
            // Handle result.error()
            println("ERROR: " + result.error())
        }
    }
    val navController = rememberNavController()
    NavHost(navController, startDestination = "channellist") {
        composable("channellist") {
            ChannelListScreen(navController = navController, channelList)
        }
        composable("messagelist/{cid}") { backStackEntry ->
            MessageListScreen(
                navController = navController,
                cid = backStackEntry.arguments?.getString("cid")!!
            )
        }
    }
}
