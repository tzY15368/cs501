package com.cs501.cs501app.buotg.database

import android.content.Context
import androidx.room.Room
import com.cs501.cs501app.buotg.entities.User

private const val DB_NAME = "buotg-db"

class AppRepository private constructor(context:Context){
    private val database:AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        DB_NAME
    ).fallbackToDestructiveMigration().build()

    suspend fun getUser(id:Int): User? = database.userDao().getUser(id)

    private val eventRepository:EventRepository = EventRepositoryImpl(database.eventDao())

    fun getEventRepository():EventRepository = eventRepository

    companion object {
        private var instance:AppRepository? = null

        fun initialize(context:Context){
            if(instance==null){
                instance = AppRepository(context)
            }
        }

        fun get():AppRepository{
            return instance?:throw IllegalStateException("db init")
        }
    }
}