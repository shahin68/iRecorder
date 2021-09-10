package com.itranslate.recorder.data.local.sources

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.itranslate.recorder.data.local.models.records.Record
import com.itranslate.recorder.data.local.sources.db.records.RecordsDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

/**
 * Local data source access point
 * Including access to all locally stored data, databases & devices storage
 *
 * implements [LocalRepository] properties
 *
 * @param recordsDao is an access point for records table through local database
 */
class LocalRepositoryImpl(
    private val recordsDao: RecordsDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): LocalRepository {
    override suspend fun insertRecord(record: Record) {
        return recordsDao.insertRecord(record)
    }

    override suspend fun deleteRecord(record: Record) {
        return recordsDao.deleteRecord(record)
    }

    override fun getRecords(): Flow<PagingData<Record>> {
        val records = recordsDao.getRecords()
        return Pager(
            PagingConfig(
                pageSize = 25,
                prefetchDistance = 5,
                enablePlaceholders = false,
                initialLoadSize = 100
            )
        ) {
            records.asPagingSourceFactory(dispatcher).invoke()
        }.flow
    }

    override fun getSortedRecords(): Flow<PagingData<Record>> {
        val records = recordsDao.getSortedRecords()
        return Pager(
            PagingConfig(
                pageSize = 25,
                prefetchDistance = 5,
                enablePlaceholders = false,
                initialLoadSize = 100
            )
        ) {
            records.asPagingSourceFactory(dispatcher).invoke()
        }.flow
    }

}