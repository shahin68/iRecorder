package com.itranslate.recorder.data.local.sources

import androidx.paging.DataSource
import com.itranslate.recorder.data.local.models.records.Record

/**
 * Local data source access point
 * Including access to all locally stored data, databases & devices storage
 */
interface LocalRepository {

    /**
     * Query to insert one [record]
     */
    suspend fun insertRecord(record: Record)

    /**
     * Query to insert list of [records]
     */
    suspend fun insertRecord(records: List<Record>)

    /**
     * Query to update a [record]
     */
    suspend fun updateRecord(record: Record)

    /**
     * Query to remove one [record]
     */
    suspend fun deleteRecord(record: Record)

    /**
     * Query to delete [Record]s with record_id the same as [recordId]
     */
    suspend fun deleteRecordsByRecordId(recordId: Int)

    /**
     * Query to clear records table
     */
    suspend fun clearAllRecords()

    /**
     * Used paging 3 [DataSource.Factory] as return type to implement paginated flow of [Record]s
     * @return Not sorted paged list of [Record]s
     */
    fun getRecords(): DataSource.Factory<Int, Record>

    /**
     * Used paging 3 [DataSource.Factory] as return type to implement paginated flow of [Record]s
     * @return Not sorted paged list of [Record]s which contain [query] in their record_name column
     */
    fun findRecordsByQuery(query: String): DataSource.Factory<Int, Record>

    /**
     * Used paging 3 [DataSource.Factory] as return type to implement paginated flow of [Record]s
     * @return Sorted paged list of [Record]s
     */
    fun getSortedRecords(): DataSource.Factory<Int, Record>

    /**
     * Used paging 3 [DataSource.Factory] as return type to implement paginated flow of [Record]s
     * @return Sorted paged list of [Record]s which contain [query] in their record_name column
     */
    fun findSortedRecordsByQuery(query: String): DataSource.Factory<Int, Record>

}