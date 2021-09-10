package com.itranslate.recorder.ui.fragments.recordings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.itranslate.recorder.data.local.models.records.Record
import com.itranslate.recorder.data.local.sources.LocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

class RecordingsViewModel(
    private val localRepository: LocalRepository
) : ViewModel() {

    suspend fun insertRecordInDb(record: Record) {
        return localRepository.insertRecord(record)
    }

    suspend fun deleteRecord(record: Record) {
        return localRepository.deleteRecord(record)
    }

    fun getSortedRecordings(): Flow<PagingData<Record>> {
        return localRepository.getSortedRecords().distinctUntilChanged().cachedIn(viewModelScope)
    }

}