package com.itranslate.recorder.data

import androidx.paging.DataSource
import androidx.paging.PagingData
import com.itranslate.recorder.data.local.models.records.Record
import kotlinx.coroutines.flow.Flow

/**
 * Main repository interface
 *
 * [Repository] is the main point of access to all other data repositories
 * For the purposes of having one main source of data for a small app, No other data
 * repository class will be accessed by any other class (including View Models)
 * other than [Repository]
 *
 * @see RepositoryImpl
 */
interface Repository {

    /**
     * Query to insert one [record]
     */
    suspend fun insertRecord(record: Record)

    /**
     * Used paging 3 [DataSource.Factory] as return type to implement paginated flow of [Record]s
     * @return Sorted paged list of [Record]s
     */
    fun getSortedRecords(): Flow<PagingData<Record>>
}