package com.paf.londonevents.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.paf.londonevents.core.model.Event

@Database(entities = [Event::class], version = 3)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun eventDao(): EventDao
}