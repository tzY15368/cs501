package com.cs501.cs501app.buotg.database.repositories

import android.content.Context
import androidx.room.Room
import com.cs501.cs501app.buotg.connection.API
import com.cs501.cs501app.buotg.connection.SafeAPIRequest
import com.cs501.cs501app.buotg.connection.StdResponse
import com.cs501.cs501app.buotg.database.AppDatabase

private const val DB_NAME = "buotg-db"

class AppRepository private constructor(context:Context) :SafeAPIRequest(){
    private val database: AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        DB_NAME
    ).fallbackToDestructiveMigration().build()

    private val userRepo = UserRepository(database)
    private val inviteRepository = InviteRepository()
    private val sharedEventRepo = SharedEventRepo(database)
    private val sharedEventParticipanceRepo = SharedEventParticipanceRepo(database)
    private val groupRepo = GroupRepository(database)
    private val eventRepository: EventRepository = EventRepository(database)
    suspend fun ping(ctx:Context):StdResponse? = apiRequest(ctx, { API.getClient().ping() })
    fun eventRepo(): EventRepository = eventRepository

    fun userRepo() = userRepo

    fun sharedEventRepo() = sharedEventRepo

    fun inviteRepository() = inviteRepository

    fun sharedEventParticipanceRepo() = sharedEventParticipanceRepo
    fun groupRepo() = groupRepo


    fun kvDao() = database.kvDao()
    fun eventDao() = database.eventDao()
    fun userDao() = database.userDao()
    fun groupDao() = database.groupDao()
    fun groupMemberDao() = database.groupMemberDao()
    fun sharedEventDao() = database.sharedEventDao()
    fun sharedEventParticipanceDao() = database.sharedEventParticipanceDao()

    fun getAppDatabase() = database

    companion object {
        private var instance: AppRepository? = null

        fun initialize(context:Context){
            if(instance ==null){
                instance = AppRepository(context)
            }
        }

        fun get(): AppRepository {
            return instance ?:throw IllegalStateException("db init")
        }
    }
}