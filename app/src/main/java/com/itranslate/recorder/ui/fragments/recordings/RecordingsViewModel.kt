package com.itranslate.recorder.ui.fragments.recordings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.itranslate.recorder.data.local.models.records.Record
import com.itranslate.recorder.data.local.sources.LocalRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

class RecordingsViewModel(
    private val localRepository: LocalRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    suspend fun insertRecordInDb(record: Record) {
        return localRepository.insertRecord(record)
    }

    suspend fun deleteRecord(record: Record) {
        return localRepository.deleteRecord(record)
    }

    fun getSortedRecordings(): Flow<PagingData<Record>> {
        val records = localRepository.getSortedRecords()
        return Pager(
            PagingConfig(
                pageSize = 25,
                prefetchDistance = 5,
                enablePlaceholders = false,
                initialLoadSize = 100
            )
        ) {
            records.asPagingSourceFactory(dispatcher).invoke()
        }.flow.distinctUntilChanged().cachedIn(viewModelScope)
    }

}