package com.cs501.cs501app.buotg

import android.app.Application
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import com.cs501.cs501app.buotg.view.thirdParty.chatRoom.ChatApplication
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.client.models.image
import io.getstream.chat.android.client.models.name
import io.getstream.chat.android.livedata.ChatDomain

open class BUOTGApplication: ChatApplication(), ClientProvider {
    override lateinit var client: ChatClient
    override fun onCreate() {
        super.onCreate()
        client = ChatClient.Builder(appContext = this, apiKey = "s2mqsdmwr6b8")
            .logLevel(ChatLogLevel.ALL)
            .build()
        val user = User().apply {
            id = "danny"
            image = "https://bit.ly/2TIt8NR"
            name = "danny"
        }
        val token = client.devToken(user.id)
        ChatDomain.Builder(client, this)
            .offlineEnabled()
            .build()
        println(token)
        client.connectUser(
            user,
            token
        ).enqueue { result ->
            if (result.isSuccess) {
                println("SUCCESS")
            } else {
                println("FAILED")
            }
        }

        AppRepository.initialize(this)
    }
}