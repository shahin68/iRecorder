package com.itranslate.recorder.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.itranslate.recorder.data.local.models.records.Record
import com.itranslate.recorder.data.local.sources.LocalRepository
import com.itranslate.recorder.data.remote.sources.RemoteRepository
import com.itranslate.recorder.di.DispatcherModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Class representing a single data source to all other parts of the project
 *
 * Provides access to [LocalRepository] & [RemoteRepository] indirectly to other classes
 * including View Models
 *
 */
class RepositoryImpl @Inject constructor(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    @DispatcherModule.IoDispatcher private val dispatcher: CoroutineDispatcher
) : Repository {

    override suspend fun insertRecord(record: Record) {
        return localRepository.insertRecord(record)
    }

    override fun getSortedRecords(): Flow<PagingData<Record>> {
        val records = localRepository.getSortedRecords()
        return Pager(
            PagingConfig(
                pageSize = 25,
                prefetchDistance = 5,
                enablePlaceholders = false,
                initialLoadSize = 25
            )
        ) {
            records.asPagingSourceFactory(dispatcher).invoke()
        }.flow
    }

}