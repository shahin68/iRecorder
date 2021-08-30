package com.itranslate.recorder.di

import android.app.Application
import androidx.room.Room
import com.itranslate.recorder.data.local.sources.db.AppDatabase
import com.itranslate.recorder.data.local.sources.db.records.RecordsDao
import com.itranslate.recorder.general.Constants.DATABASE_NAME
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

/**
 * Module responsible for providing single instance of [AppDatabase] & [RecordsDao]
 */
val dbModule = module {
    single { provideDatabase(androidApplication()) }
    single { provideRecordsDao(get()) }
}

private fun provideDatabase(application: Application): AppDatabase {
    return Room.databaseBuilder(application, AppDatabase::class.java, DATABASE_NAME)
        .fallbackToDestructiveMigration()
        .build()
}

private fun provideRecordsDao(appDatabase: AppDatabase) = appDatabase.recordsDao()