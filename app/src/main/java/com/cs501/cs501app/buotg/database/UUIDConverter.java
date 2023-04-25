package com.cs501.cs501app.buotg.database;

import android.util.Log;

import androidx.room.TypeConverter;

import java.util.UUID;

public class UUIDConverter {

    @TypeConverter
    public static String fromUUID(UUID uuid) {
        Log.d("UUIDConverter", "fromUUID: " + uuid.toString());
        return uuid.toString();
    }

    @TypeConverter
    public static UUID uuidFromString(String string) {
        return UUID.fromString(string);
    }
}