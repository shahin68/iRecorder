package com.itranslate.recorder.data.local.sources

import androidx.paging.DataSource
import com.itranslate.recorder.data.local.models.records.Record
import com.itranslate.recorder.data.local.sources.db.records.RecordsDao

/**
 * Local data source access point
 * Including access to all locally stored data, databases & devices storage
 *
 * implements [LocalRepository] properties
 *
 * @param recordsDao is an access point for records table through local database
 */
class LocalRepositoryImpl(
    private val recordsDao: RecordsDao
): LocalRepository {
    override suspend fun insertRecord(record: Record) {
        return recordsDao.insertRecord(record)
    }

    override suspend fun deleteRecord(record: Record) {
        return recordsDao.deleteRecord(record)
    }

    override fun getRecords(): DataSource.Factory<Int, Record> {
        return recordsDao.getRecords()
    }

    override fun getSortedRecords(): DataSource.Factory<Int, Record> {
        return recordsDao.getSortedRecords()
    }

}