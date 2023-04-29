package com.cs501.cs501app.buotg.view.thirdParty.chatRoom

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import com.cs501.cs501app.buotg.view.dayNightTheme.ComposeChatTheme
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.QueryChannelsRequest
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.models.Filters

class ChatRoomActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val targetApp = (application as ChatApplication)
        val chatClient = (application as ChatApplication).client
        val name = (application as ChatApplication).userName
        setContent {
            ComposeChatTheme {
                Surface(color = MaterialTheme.colors.background) {
                    if (name != null) {
                        ChatApp(chatClient, name, targetApp)
                    }
                }
            }
        }
    }
}


suspend fun fetchChannels(client: ChatClient, channelList: MutableState<List<Channel>>, request: QueryChannelsRequest) {
    client.queryChannels(request).enqueue { result ->
        if (result.isSuccess) {
            for (res in result.data()) {
                println("CHECK: " + res.cid)
            }
            channelList.value = result.data()
        } else {
            // Handle result.error()
            println("ERROR: " + result.error())
        }
    }
}


@Composable
fun ChatApp(client: ChatClient, name: String, targetApp: ChatApplication) {
    Log.d("ChatApp", "ChatApp: $name")
    val channelList = rememberSaveable { mutableStateOf(listOf<Channel>()) }
    var request = QueryChannelsRequest(
        filter = Filters.and(
            Filters.eq("type", "messaging"),
            Filters.`in`("members", name)
        ),
        offset = 0,
        limit = 10
    ).apply {
        watch = true
        state = true
    }
    val ctx = LocalContext.current

    client.queryChannels(request).enqueue { result ->
        if (result.isSuccess) {
            for (res in result.data()) {
                println("CHECK_LIST: " + res.cid)
            }
            channelList.value = result.data()

        } else {
            // Handle result.error()
            println("ERROR_LIST: " + result.error())
        }
    }
    val navController = rememberNavController()
    LaunchedEffect(targetApp.clearCache) {
        val currentUser = AppRepository.get().userDao().getCurrentUser()
        var new_name = currentUser?.full_name!!
        if (new_name == "abc") {
            new_name = "abc-1"
        }
        println("CLEAR_CACHE GET CHANGE:$new_name " + targetApp.clearCache.value)
        request = QueryChannelsRequest(
            filter = Filters.and(
                Filters.eq("type", "messaging"),
                Filters.`in`("members", new_name)
            ),
            offset = 0,
            limit = 10
        ).apply {
            watch = true
            state = true
        }
        channelList.value = listOf<Channel>()
        client.queryChannels(request).enqueue { result ->
            if (result.isSuccess) {
                for (res in result.data()) {
                    println("CHECK_LIST: " + res.cid)
                }
                channelList.value = result.data()

            } else {
                // Handle result.error()
                println("ERROR_LIST: " + result.error())
            }
        }
    }

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
