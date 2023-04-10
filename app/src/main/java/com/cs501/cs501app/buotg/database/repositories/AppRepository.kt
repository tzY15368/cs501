package com.cs501.cs501app.buotg.database.repositories

import android.content.Context
import androidx.room.Room
import com.cs501.cs501app.buotg.database.AppDatabase
import com.cs501.cs501app.buotg.database.entities.User

private const val DB_NAME = "buotg-db"

class AppRepository private constructor(context:Context){
    private val database: AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        DB_NAME
    ).fallbackToDestructiveMigration().build()

    suspend fun getUser(id:Int): User? = database.userDao().getUser(id)

    private val eventRepository: EventRepository = EventRepositoryImpl(database.eventDao())

    fun getEventRepository(): EventRepository = eventRepository

    private val userRepo = UserRepository(database)
    fun userRepo() = userRepo

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