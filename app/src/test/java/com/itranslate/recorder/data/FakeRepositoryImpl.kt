package com.itranslate.recorder.data

import androidx.paging.PagingData
import com.itranslate.recorder.data.local.models.records.Record
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class FakeRepositoryImpl : Repository {

    private val _recordsFlow: MutableStateFlow<PagingData<Record>> =
        MutableStateFlow(PagingData.empty())
    val recordsFlow: Flow<PagingData<Record>> = _recordsFlow

    private val recordsList: MutableList<Record> = mutableListOf()

    override suspend fun insertRecord(record: Record) {
        recordsList.add(record)
        _recordsFlow.value = PagingData.from(recordsList)
    }

    override fun getSortedRecords(): Flow<PagingData<Record>> {
        return flowOf(
            PagingData.from(
                recordsList
            )
        )
    }


}