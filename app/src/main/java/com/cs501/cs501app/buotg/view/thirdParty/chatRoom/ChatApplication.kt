package com.cs501.cs501app.buotg.view.thirdParty.chatRoom

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import com.cs501.cs501app.buotg.BUOTGApplication
import com.cs501.cs501app.buotg.ClientProvider
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.client.models.*
import io.getstream.chat.android.livedata.ChatDomain

open class ChatApplication : Application(), ClientProvider {
    override lateinit var client: ChatClient
    open var userName: String? = null
    open val channelCreated = mutableStateOf(false)
    open val clientInitialized  = mutableStateOf(false)
    open val channelCreatedTrigger = mutableStateOf(false)
    open var clearCache = mutableStateOf(false)
    override fun onCreate() {
        super.onCreate()
    }
    open suspend fun connectUser() {}

    open suspend fun create_channel(channelName : String) {}
}

