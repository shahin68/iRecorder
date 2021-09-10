package com.itranslate.recorder.ui.fragments.home

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel(
    private val localRepository: LocalRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    suspend fun insertRecordInDb(record: Record) {
        return localRepository.insertRecord(record)
    }

    private val _recordsFlow: MutableStateFlow<PagingData<Record>> =
        MutableStateFlow(PagingData.empty())
    val recordsFlow: Flow<PagingData<Record>> = _recordsFlow

    fun getDbRecords() {
        viewModelScope.launch {
            val records = localRepository.getRecords()
            Pager(
                PagingConfig(
                    pageSize = 25,
                    prefetchDistance = 5,
                    enablePlaceholders = false,
                    initialLoadSize = 100
                )
            ) {
                records.asPagingSourceFactory(dispatcher).invoke()
            }.flow.cachedIn(viewModelScope).collectLatest {
                val previousValue = _recordsFlow.value
                _recordsFlow.compareAndSet(
                    previousValue,
                    it
                )
            }
        }
    }

}