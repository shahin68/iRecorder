package com.itranslate.recorder.data

import androidx.paging.PagingData
import com.itranslate.recorder.data.local.models.records.Record
import com.itranslate.recorder.data.local.sources.LocalRepository
import com.itranslate.recorder.general.ConstantsTest.RECORD_0
import com.itranslate.recorder.general.ConstantsTest.RECORD_1
import com.itranslate.recorder.general.ConstantsTest.RECORD_2
import com.itranslate.recorder.general.ConstantsTest.RECORD_3
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class FakeLocalRepositoryImpl : LocalRepository {

    private val _recordsFlow: MutableStateFlow<PagingData<Record>> =
        MutableStateFlow(PagingData.empty())
    val recordsFlow: Flow<PagingData<Record>> = _recordsFlow

    private val recordsList: MutableList<Record> = mutableListOf()

    override suspend fun insertRecord(record: Record) {
        recordsList.add(record)
        _recordsFlow.value = PagingData.from(recordsList)
    }

    override suspend fun deleteRecord(record: Record) {
        recordsList.remove(record)
    }

    override fun getRecords(): Flow<PagingData<Record>> {
        return flowOf(
            PagingData.from(
                recordsList
            )
        )
    }

    override fun getSortedRecords(): Flow<PagingData<Record>> {
        return flowOf(
            PagingData.from(
                listOf(
                    RECORD_0,
                    RECORD_1,
                    RECORD_2,
                    RECORD_3
                )
            )
        )
    }

}