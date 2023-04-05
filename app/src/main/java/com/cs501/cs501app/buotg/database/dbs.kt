package com.cs501.cs501app.buotg.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cs501.cs501app.buotg.entities.Event
import com.cs501.cs501app.buotg.entities.KVEntry
import com.cs501.cs501app.buotg.entities.SharedEvent
import com.cs501.cs501app.buotg.entities.User

@Database(
    entities = [Event::class, SharedEvent::class, User::class, KVEntry::class],
    version = 2,
    autoMigrations = [AutoMigration(from = 1, to = 2)]
)
@TypeConverters(DateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun kvDao(): KVDao
}