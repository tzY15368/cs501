package com.cs501.cs501app.buotg.database.repositories

import android.content.Context
import com.cs501.cs501app.buotg.connection.API
import com.cs501.cs501app.buotg.connection.InviteResponse
import com.cs501.cs501app.buotg.connection.SafeAPIRequest

class InviteRepository : SafeAPIRequest() {
    suspend fun upsertInvite(
        ctx: Context,
        groupID: Int,
        user_email: String,
        status: API.InviteStatus
    ) {
        val res = apiRequest(ctx, { API.getClient().invite(groupID, user_email, status) })
    }

    suspend fun listInvites(ctx: Context): InviteResponse? {
        val res = apiRequest(ctx, { API.getClient().invite_list() })
        return res
    }

    suspend fun listGroupInvites(ctx: Context, groupID: Int): InviteResponse? {
        val res = apiRequest(ctx, { API.getClient().invite_list_group(groupID) })
        return res
    }
}