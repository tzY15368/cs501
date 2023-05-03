package com.cs501.cs501app.buotg.database.repositories
import android.content.Context
import com.cs501.cs501app.buotg.connection.*
import com.cs501.cs501app.buotg.database.AppDatabase

class GroupMemberRepo(db: AppDatabase) : SafeAPIRequest() {
    private val groupMemberDao = db.groupMemberDao()

    suspend fun getGroupMembers(ctx: Context, ID: Int): GMLResponse? {
        val res = apiRequest(ctx, {API.getClient().group_member_list(ID)})
        res?.let { groupMemberDao.getGroupMembersById(ID) }
        return res
    }

}