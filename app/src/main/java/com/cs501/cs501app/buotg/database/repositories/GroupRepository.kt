package com.cs501.cs501app.buotg.database.repositories

import android.content.Context
import com.cs501.cs501app.buotg.connection.*
import com.cs501.cs501app.buotg.database.AppDatabase
import retrofit2.Response
import java.util.UUID

class GroupRepository(
    db: AppDatabase
) : SafeAPIRequest() {
    private val GroupDao = db.groupDao()
    suspend fun getGroup(ctx: Context, ID: Int): GroupResponse? {
        val res = apiRequest(ctx, {API.getClient().get_group(ID)})
        res?.let { GroupDao.upsert(it.group) }
        return res
    }

    suspend fun listGroups(ctx:Context): GroupListResponse? {
        val res = apiRequest(ctx, {API.getClient().group_list()})
        res?.let { GroupDao.upsertAll(it.groups) }
        return res
    }

    suspend fun createGroup(ctx: Context, name: String, desc:String) : StdResponse?{
        val res = apiRequest(ctx, {API.getClient().create_group(name,desc)})
        return res
    }

    suspend fun deleteGroup(ctx: Context, ID: Int) : StdResponse?{
        val res = apiRequest(ctx, {API.getClient().delete_group(ID)})
        return res
    }

    suspend fun joinGroup(ctx: Context, ID: Int, UID: UUID) : StdResponse?{
        val res = apiRequest(ctx, {API.getClient().add_group_member(ID, UID)})
        return res
    }

    suspend fun leaveGroup(ctx: Context, ID: Int, UID: UUID) : StdResponse?{
        val res = apiRequest(ctx, {API.getClient().remove_group_member(ID, UID)})
        return res
    }

    suspend fun getGroupMembers(ctx: Context, ID: Int) : GMLResponse?{
        val res = apiRequest(ctx, {API.getClient().group_member_list(ID)})
        return res
    }
}