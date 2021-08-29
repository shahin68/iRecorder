package com.itranslate.recorder.ui.fragments.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.itranslate.recorder.data.Repository
import com.itranslate.recorder.data.local.models.records.Record
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    suspend fun insertRecord(record: Record) {
        return repository.insertRecord(record)
    }

    private val _recordsFlow: MutableStateFlow<PagingData<Record>> =
        MutableStateFlow(PagingData.empty())
    val recordsFlow: Flow<PagingData<Record>> = _recordsFlow

    fun getSortedRecords() {
        viewModelScope.launch {
            repository.getSortedRecords().cachedIn(viewModelScope).collectLatest {
                val previousValue = _recordsFlow.value
                _recordsFlow.compareAndSet(
                    previousValue,
                    it
                )
            }
        }
    }

}