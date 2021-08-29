package com.itranslate.recorder.ui.fragments.recordings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.itranslate.recorder.data.Repository
import com.itranslate.recorder.data.local.models.records.Record
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class RecordingsViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    fun getSortedRecordings(): Flow<PagingData<Record>> {
        return repository.getSortedRecords().cachedIn(viewModelScope)
    }

}