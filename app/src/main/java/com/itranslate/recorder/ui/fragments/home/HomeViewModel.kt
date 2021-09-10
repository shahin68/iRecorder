package com.itranslate.recorder.ui.fragments.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.itranslate.recorder.data.local.models.records.Record
import com.itranslate.recorder.data.local.sources.LocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel(
    private val localRepository: LocalRepository
) : ViewModel() {

    suspend fun insertRecordInDb(record: Record) {
        return localRepository.insertRecord(record)
    }

    private val _recordsFlow: MutableStateFlow<PagingData<Record>> =
        MutableStateFlow(PagingData.empty())
    val recordsFlow: Flow<PagingData<Record>> = _recordsFlow

    fun getDbRecords() {
        viewModelScope.launch {
            localRepository.getRecords().cachedIn(viewModelScope).collectLatest {
                val previousValue = _recordsFlow.value
                _recordsFlow.compareAndSet(
                    previousValue,
                    it
                )
            }
        }
    }

}