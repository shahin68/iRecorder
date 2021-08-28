package com.itranslate.recorder.di

import android.content.Context
import androidx.room.Room
import com.itranslate.recorder.data.local.sources.db.AppDatabase
import com.itranslate.recorder.data.local.sources.db.records.RecordsDao
import com.itranslate.recorder.general.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Module responsible for providing single instance of [AppDatabase] & [RecordsDao]
 */
@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(appContext, AppDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideRecordsDao(appDatabase: AppDatabase) = appDatabase.recordsDao()

}