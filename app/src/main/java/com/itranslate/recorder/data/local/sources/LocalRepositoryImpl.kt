package com.itranslate.recorder.data.local.sources

import androidx.paging.DataSource
import com.itranslate.recorder.data.local.models.records.Record
import com.itranslate.recorder.data.local.sources.db.records.RecordsDao
import javax.inject.Inject

/**
 * Local data source access point
 * Including access to all locally stored data, databases & devices storage
 *
 * implements [LocalRepository] properties
 *
 * @param recordsDao is an access point for records table through local database
 */
class LocalRepositoryImpl @Inject constructor(
    private val recordsDao: RecordsDao
): LocalRepository {
    override suspend fun insertRecord(record: Record) {
        return recordsDao.insertRecord(record)
    }

    override suspend fun insertRecord(records: List<Record>) {
        return recordsDao.insertRecord(records)
    }

    override suspend fun updateRecord(record: Record) {
        return recordsDao.updateRecord(record)
    }

    override suspend fun deleteRecord(record: Record) {
        return recordsDao.deleteRecord(record)
    }

    override suspend fun deleteRecordsByRecordId(recordId: Int) {
        return recordsDao.deleteRecordsByRecordId(recordId)
    }

    override suspend fun clearAllRecords() {
        return recordsDao.clearAllRecords()
    }

    override fun getRecords(): DataSource.Factory<Int, Record> {
        return recordsDao.getRecords()
    }

    override fun findRecordsByQuery(query: String): DataSource.Factory<Int, Record> {
        return recordsDao.findRecordsByQuery(query)
    }

    override fun getSortedRecords(): DataSource.Factory<Int, Record> {
        return recordsDao.getSortedRecords()
    }

    override fun findSortedRecordsByQuery(query: String): DataSource.Factory<Int, Record> {
        return recordsDao.findSortedRecordsByQuery(query)
    }


}