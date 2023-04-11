package com.cs501.cs501app.buotg.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cs501.cs501app.buotg.database.dao.*
import com.cs501.cs501app.buotg.database.entities.*

@Database(
    entities = [
        Event::class,
        SharedEvent::class,
        SharedEventParticipance::class,
        User::class,
        KVEntry::class,
        Group::class,
        GroupMember::class,
    ], version = 8
)
@TypeConverters(DateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun eventDao(): EventDao
    abstract fun kvDao(): KVDao

    abstract fun groupDao(): GroupDao

    abstract fun groupMemberDao(): GroupMemberDao

    abstract fun sharedEventDao(): SharedEventDao

    abstract fun sharedEventParticipanceDao(): SharedEventParticipanceDao
}