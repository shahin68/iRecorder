package com.itranslate.recorder.data.local.sources.db.records

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth
import com.itranslate.recorder.data.local.models.records.Record
import com.itranslate.recorder.data.local.sources.db.AppDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class RecordsDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private lateinit var recordsDao: RecordsDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        recordsDao = database.recordsDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun insertRecordItem_shouldPass() {
        runBlockingTest {
            val recordItem = Record(
                recordId = 1,
                recordName = "Recording 0",
                recordPath = "path",
                recordDuration = "00:00"
            )
            recordsDao.insertRecord(recordItem)

            val recordsTable = recordsDao.getRecordsList()
            /**
             * Because recordItem has recordId as null. We need to see if the table items
             * contain at least a property value of recordName to let the test pass
             */
            Truth.assertThat(recordsTable.map { it.recordName }).contains(recordItem.recordName)
        }
    }

    /**
     * To test if room is deleting properly we actually insert a new item & then delete the same
     * item immediately
     * This means records table now should not contain this item at the end
     *
     * This time also, we will set recordId manually to ensure we have the exact same item throughout
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun removeRecordItem_shouldPass() {
        runBlockingTest {
            val recordItem = Record(
                recordId = 1,
                recordName = "Recording 0",
                recordPath = "path",
                recordDuration = "00:00"
            )
            recordsDao.insertRecord(recordItem)
            recordsDao.deleteRecord(recordItem)

            val recordsTable = recordsDao.getRecordsList()
            Truth.assertThat(recordsTable.map { it.recordName })
                .doesNotContain(recordItem.recordName)
        }
    }

    /**
     * If we insert 2 records with the same recordName,
     * can we find this exact item later?
     * We expect to actually find 2 items
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun findRecordItem_shouldPass() {
        runBlockingTest {
            val recordItem1 = Record(
                recordId = 1,
                recordName = "Recording 0",
                recordPath = "path",
                recordDuration = "00:00"
            )
            val recordItem2 = Record(
                recordId = 2,
                recordName = "Recording 0",
                recordPath = "path",
                recordDuration = "00:00"
            )
            recordsDao.insertRecord(recordItem1)
            recordsDao.insertRecord(recordItem2)

            val wantedRecords = recordsDao.findRecordsByExactRecordName(recordItem1.recordName)

            Truth.assertThat(wantedRecords).isNotEmpty() // records must not be empty
            Truth.assertThat(wantedRecords.size).isEqualTo(2) // there must be 2 records
        }
    }
}