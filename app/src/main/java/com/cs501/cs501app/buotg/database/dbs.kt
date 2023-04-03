package com.cs501.cs501app.buotg.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cs501.cs501app.buotg.entities.Event
import com.cs501.cs501app.buotg.entities.SharedEvent
import com.cs501.cs501app.buotg.entities.User

@Database(entities = [Event::class, SharedEvent::class, User::class],version=1)
@TypeConverters(DateTimeConverter::class)
abstract class AppDatabase: RoomDatabase(){
    abstract fun userDao(): UserDao
}