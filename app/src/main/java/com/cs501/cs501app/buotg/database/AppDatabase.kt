package com.cs501.cs501app.buotg.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cs501.cs501app.buotg.database.dao.EventDao
import com.cs501.cs501app.buotg.database.dao.KVDao
import com.cs501.cs501app.buotg.database.dao.UserDao
import com.cs501.cs501app.buotg.database.entities.Event
import com.cs501.cs501app.buotg.database.entities.KVEntry
import com.cs501.cs501app.buotg.database.entities.SharedEvent
import com.cs501.cs501app.buotg.database.entities.User

@Database(entities = [Event::class, SharedEvent::class, User::class, KVEntry::class], version=5)
@TypeConverters(DateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun eventDao(): EventDao
    abstract fun kvDao(): KVDao
}