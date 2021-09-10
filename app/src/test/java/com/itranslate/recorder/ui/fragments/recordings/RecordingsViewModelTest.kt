package com.itranslate.recorder.ui.fragments.recordings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.google.common.truth.Truth
import com.itranslate.recorder.data.FakeLocalRepositoryImpl
import com.itranslate.recorder.data.local.models.records.Record
import com.itranslate.recorder.general.ConstantsTest.RECORD_0
import com.itranslate.recorder.general.ConstantsTest.RECORD_4
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RecordingsViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var viewModel: RecordingsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = RecordingsViewModel(FakeLocalRepositoryImpl())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun getRecordsFlowContainsRecord0_shouldPass() {
        testDispatcher.runBlockingTest {

            val differ = AsyncPagingDataDiffer(
                diffCallback = MyDiffCallback(),
                updateCallback = ListCallback(),
                workerDispatcher = testDispatcher
            )

            viewModel.getSortedRecordings().collectLatest {
                differ.submitData(it)
            }

            Truth.assertThat(differ.snapshot().items).contains(RECORD_0)
        }
    }

    @Test
    fun getRecordsFlowDoesNotContainRecord4_shouldPass() {
        testDispatcher.runBlockingTest {

            val differ = AsyncPagingDataDiffer(
                diffCallback = MyDiffCallback(),
                updateCallback = ListCallback(),
                workerDispatcher = testDispatcher
            )

            viewModel.getSortedRecordings().collectLatest {
                differ.submitData(it)
            }

            Truth.assertThat(differ.snapshot().items).doesNotContain(RECORD_4)
        }
    }

    inner class ListCallback : ListUpdateCallback {
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
    }

    inner class MyDiffCallback : DiffUtil.ItemCallback<Record>() {
        override fun areItemsTheSame(oldItem: Record, newItem: Record): Boolean {
            return newItem == oldItem
        }

        override fun areContentsTheSame(oldItem: Record, newItem: Record): Boolean {
            return newItem.recordName.equals(oldItem.recordName, ignoreCase = false)
        }
    }
}