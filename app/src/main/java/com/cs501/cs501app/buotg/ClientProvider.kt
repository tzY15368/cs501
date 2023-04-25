package com.cs501.cs501app.buotg

import io.getstream.chat.android.client.ChatClient

interface ClientProvider {
    var client: ChatClient
}