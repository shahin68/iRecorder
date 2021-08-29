package com.itranslate.recorder.ui.fragments.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.itranslate.recorder.data.FakeRepositoryImpl
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
        viewModel = HomeViewModel(FakeRepositoryImpl())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun insertRecord_recordsFlowAtLeastIsNotNull_shouldPass() {
        testDispatcher.runBlockingTest {
            val newRecord = Record(recordName = "Recording 0")
            viewModel.insertRecord(newRecord)

            viewModel.getSortedRecords()

            val records = viewModel.recordsFlow.firstOrNull()

            Truth.assertThat(records).isNotNull()
        }
    }

}