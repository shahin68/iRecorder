package com.itranslate.recorder.data.local.sources.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.itranslate.recorder.data.local.models.records.Record
import com.itranslate.recorder.data.local.sources.db.records.RecordsDao

/**
 * Database Version 1
 *
 * Instances of this class will give access to [RecordsDao]
 * @see RecordsDao
 *
 * To provide possibility of running auto migrations, "exportSchema" is set to true
 */
@Database(
    version = 1,
    entities = [
        Record::class
    ],
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recordsDao(): RecordsDao
}
