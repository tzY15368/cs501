package com.cs501.cs501app.buotg

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleCoroutineScope
import com.cs501.cs501app.buotg.database.entities.USER_TOKEN_KEY
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import com.cs501.cs501app.buotg.view.thirdParty.chatRoom.ChatApp
import com.cs501.cs501app.buotg.view.thirdParty.chatRoom.ChatApplication
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.QueryUsersRequest
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.client.models.*
import io.getstream.chat.android.livedata.ChatDomain
import kotlinx.coroutines.*

open class BUOTGApplication: ChatApplication(), ClientProvider {
    override lateinit var client: ChatClient
    override var userName: String? = null
    override val channelCreated = mutableStateOf(false)
    override val clientInitialized  = mutableStateOf(false)
    override val channelCreatedTrigger = mutableStateOf(false)
    override var clearCache = mutableStateOf(false)

    companion object {
        @Volatile private var INSTANCE: BUOTGApplication? = null

        fun getInstance(context: Context): BUOTGApplication =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: BUOTGApplication().also { INSTANCE = it }
            }
    }

    override fun onCreate() {
        super.onCreate()
        client = ChatClient.Builder(appContext = this, apiKey = "s2mqsdmwr6b8")
            .logLevel(ChatLogLevel.ALL)
            .build()
        ChatDomain.Builder(client, this)
            .offlineEnabled()
            .build()
        AppRepository.initialize(this)
        runBlocking {
            connectUser()
        }
        clientInitialized.value = true
    }

    override suspend fun connectUser() {
        val currentUser = AppRepository.get().userDao().getCurrentUser()
        if (currentUser == null) {
            Log.d("BUOTGApplication", "No current user")
        } else {
            userName = currentUser.full_name
            if (userName == "abc") {
                userName = "abc-1"
            }
            val user0 = User().apply {
                id = userName as String//userName as String
                name = userName as String//userName as String
            }
            val token0 = client.devToken(user0.id)
            client.connectUser(
                user0,
                token0
            ).enqueue { result ->
                if (result.isSuccess) {
                    println("SUCCESS register USER " + user0.id)
                } else {
                    println("FAILED")
                    println("ERROR: " + result.error())
                }
            }
//            delay(1000)
//            client.disconnect()

//            val user = User().apply {
//                id = "bostonuniversity"//userName as String
//                name = "bostonuniversity"//userName as String
//            }
//            ChatDomain.Builder(client, this)
//                .offlineEnabled()
//                .build()
//            val token = client.devToken(user.id)
//            client.connectUser(
//                user,
//                token
//            ).enqueue { result ->
//                if (result.isSuccess) {
//                    println("SUCCESS CONNECTING ADMIN USER " + user.id)
//                } else {
//                    println("FAILED")
//                    println("ERROR: " + result.error())
//                }
//            }

//            create_channel("test2")
        }
    }

    override suspend fun create_channel(channelName : String) {
        val channelClient = client.channel(channelType = "messaging", channelId = channelName)
        println("CREATING CHANNEL$channelName")
        val extraData = mutableMapOf<String, Any>(
            "name" to "$channelName channel",
        )
        channelClient.create(members = emptyList(), extraData = extraData).enqueue { result ->
            if (result.isSuccess) {
                println("SUCCESS")
                channelCreated.value = true
                channelCreatedTrigger.value = !channelCreatedTrigger.value
            } else {
                println("FAILED")
                println("ERROR: " + result.error())
            }
        }

        delay(500)
        println("Creating channel $channelName")
        userName?.let {
            channelClient.addMembers(it).enqueue { result ->
                if (result.isSuccess) {
                    println("SUCCESS ADDING MEMBER")
                } else {
                    println("FAILED ADDING MEMBER")
                    println("ERROR: " + result.error())
                }
            }
        }

//        userName?.let {
//            client.channel("messaging:" + channelName).addMembers(it).enqueue { result ->
//                if (result.isSuccess) {
//                    println("SUCCESS ADDING MEMBER")
//                } else {
//                    println("FAILED ADDING MEMBER")
//                    println("ERROR: " + result.error())
//                }
//            }
//        }
    }

    @Composable
    fun ConnectAndShowChatApp() {
        // Get the coroutine scope for this composable
        val coroutineScope = rememberCoroutineScope()

        // Use LaunchedEffect to call the connectUser() function
        if (clientInitialized.value) {
            LaunchedEffect(Unit) {
                coroutineScope.launch {
                    connectUser()
                }
            }
        }

        // Show ChatApp if channelCreated is true
        if (channelCreated.value) {
            Log.d("BUOTGApplication", "Channel created")
//            userName?.let { ChatApp(client = client, name = it) }
            channelCreated.value = false
        }
    }
}

