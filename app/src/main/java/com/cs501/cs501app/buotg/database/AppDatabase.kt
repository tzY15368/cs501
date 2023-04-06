package com.cs501.cs501app.buotg.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cs501.cs501app.buotg.database.dao.EventDao
import com.cs501.cs501app.buotg.database.dao.KVDao
import com.cs501.cs501app.buotg.database.dao.UserDao
import com.cs501.cs501app.buotg.entities.Event
import com.cs501.cs501app.buotg.entities.KVEntry
import com.cs501.cs501app.buotg.entities.SharedEvent
import com.cs501.cs501app.buotg.entities.User

@Database(entities = [Event::class, SharedEvent::class, User::class, KVEntry::class], version=3)
@TypeConverters(DateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun eventDao(): EventDao
    abstract fun kvDao(): KVDao
}