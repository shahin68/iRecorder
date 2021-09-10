package com.itranslate.recorder.data.local.sources

import androidx.paging.DataSource
import androidx.paging.PagingData
import com.itranslate.recorder.data.local.models.records.Record
import kotlinx.coroutines.flow.Flow

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
     * Query to remove one [record]
     */
    suspend fun deleteRecord(record: Record)

    /**
     * Used paging 3 [DataSource.Factory] as return type to implement paginated flow of [Record]s
     * @return Not sorted paged list of [Record]s
     */
    fun getRecords(): Flow<PagingData<Record>>

    /**
     * Used paging 3 [DataSource.Factory] as return type to implement paginated flow of [Record]s
     * @return Sorted paged list of [Record]s
     */
    fun getSortedRecords(): Flow<PagingData<Record>>


}