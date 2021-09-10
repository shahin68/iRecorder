package com.itranslate.recorder.ui.fragments.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.itranslate.recorder.data.FakeLocalRepositoryImpl
import com.itranslate.recorder.data.local.models.records.Record
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = HomeViewModel(FakeLocalRepositoryImpl())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun insertRecord_recordsFlowAtLeastIsNotNull_shouldPass() {
        testDispatcher.runBlockingTest {
            val newRecord = Record(
                recordId = 0,
                recordName = "Recording",
                recordPath = "RecordPath",
                recordDuration = "00:00"
            )
            viewModel.insertRecordInDb(newRecord)

            viewModel.getDbRecords()

            val records = viewModel.recordsFlow.firstOrNull()

            Truth.assertThat(records).isNotNull()
        }
    }

}