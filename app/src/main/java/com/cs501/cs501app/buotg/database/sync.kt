package com.cs501.cs501app.buotg.database

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import com.cs501.cs501app.buotg.connection.API
import com.cs501.cs501app.buotg.connection.SafeAPIRequest
import com.cs501.cs501app.buotg.database.entities.*
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import com.google.gson.annotations.SerializedName

data class SyncData(
    @SerializedName("users") val users: List<User>,
    @SerializedName("groups") val groups: List<Group>,
    @SerializedName("group_members") val groupMembers: List<GroupMember>,
    @SerializedName("events") val events: List<Event>,
    @SerializedName("shared_events") val sharedEvents: List<SharedEvent>,
    @SerializedName("shared_event_participances") val sharedEventParticipances: List<SharedEventParticipance>,
)

class SyncRepo(

) : SafeAPIRequest() {
    private val db: SupportSQLiteDatabase =
        AppRepository.get().getAppDatabase().openHelper.writableDatabase
    private val appRepository = AppRepository.get()
    suspend fun sync(ctx: Context) {
        println("begin sync, locking db")
        // lock table, push all data to remote, remote will return all entities not present in local db.
        db.beginTransactionNonExclusive()
        try {
            val syncData = SyncData(
                users = appRepository.userDao().listUsers(),
                groups = appRepository.groupDao().listGroups(),
                groupMembers = appRepository.groupMemberDao().listGroupMembers(),
                events = appRepository.eventDao().listEvents(),
                sharedEvents = appRepository.sharedEventDao().listSharedEvents(),
                sharedEventParticipances = appRepository.sharedEventParticipanceDao().listSharedEventParticipances(),
            )
            val syncRes = apiRequest(ctx, { API.getClient().sync(syncData) })
            syncRes?.let {
                val itt = it.data
                appRepository.userDao().upsertAll(itt.users)
                appRepository.groupDao().upsertAll(itt.groups)
                appRepository.groupMemberDao().upsertAll(itt.groupMembers)
                appRepository.eventDao().upsertAll(itt.events)
                appRepository.sharedEventDao().upsertAll(itt.sharedEvents)
                appRepository.sharedEventParticipanceDao().upsertAll(itt.sharedEventParticipances)
            }
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            println("sync failed")
            e.printStackTrace()
        } finally {
            db.endTransaction()
        }
    }
}