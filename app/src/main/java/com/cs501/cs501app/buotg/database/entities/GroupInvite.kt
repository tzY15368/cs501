package com.cs501.cs501app.buotg.database.entities

import com.cs501.cs501app.buotg.connection.API

data class GroupInvite(
    val group_id: Int,
    val user_email: String,
    val status: API.InviteStatus,
)