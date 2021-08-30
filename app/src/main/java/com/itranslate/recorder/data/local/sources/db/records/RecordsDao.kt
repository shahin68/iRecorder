package com.itranslate.recorder.data.local.sources.db.records

import androidx.paging.DataSource
import androidx.room.*
import com.itranslate.recorder.data.local.models.records.Record

/**
 * Data access object providing access to query database
 */
@Dao
interface RecordsDao {

    /**
     * Query to insert one [record]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: Record)

    /**
     * Query to insert list of [records]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(records: List<Record>)

    /**
     * Query to update a [record]
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateRecord(record: Record)

    /**
     * Query to remove one [record]
     */
    @Delete
    suspend fun deleteRecord(record: Record)

    /**
     * Query to delete [Record]s with record_id the same as [recordId]
     */
    @Query("DELETE FROM records WHERE record_id LIKE :recordId")
    suspend fun deleteRecordsByRecordId(recordId: Int)

    /**
     * Query to clear records table
     */
    @Query("DELETE FROM records")
    suspend fun clearAllRecords()

    /**
     * Get list of records
     * @return list of [Record]s
     */
    @Query("SELECT * FROM records")
    fun getRecordsList(): List<Record>

    /**
     * Find records with given [recordId]
     * @return list of [Record]s
     */
    @Query("SELECT * FROM records WHERE record_id LIKE :recordId")
    fun findRecordsByRecordId(recordId: Int): List<Record>

    /**
     * Find records with given [recordName]
     * @return list of [Record]s
     */
    @Query("SELECT * FROM records WHERE record_name LIKE :recordName")
    fun findRecordsByExactRecordName(recordName: String): List<Record>

    /**
     * Used paging 3 [DataSource.Factory] as return type to implement paginated flow of [Record]s
     * @return Not sorted paged list of [Record]s
     */
    @Transaction
    @Query("SELECT * FROM records")
    fun getRecords(): DataSource.Factory<Int, Record>

    /**
     * Used paging 3 [DataSource.Factory] as return type to implement paginated flow of [Record]s
     * @return Not sorted paged list of [Record]s which contain [query] in their record_name column
     */
    @Transaction
    @Query("SELECT * FROM records WHERE record_name LIKE '%' || :query || '%'")
    fun findRecordsByQuery(query: String): DataSource.Factory<Int, Record>

    /**
     * Used paging 3 [DataSource.Factory] as return type to implement paginated flow of [Record]s
     * @return Sorted paged list of [Record]s
     */
    @Transaction
    @Query("SELECT * FROM records ORDER BY record_name ASC")
    fun getSortedRecords(): DataSource.Factory<Int, Record>

    /**
     * Used paging 3 [DataSource.Factory] as return type to implement paginated flow of [Record]s
     * @return Sorted paged list of [Record]s which contain [query] in their record_name column
     */
    @Transaction
    @Query("SELECT * FROM records WHERE record_name LIKE '%' || :query || '%' ORDER BY record_name ASC")
    fun findSortedRecordsByQuery(query: String): DataSource.Factory<Int, Record>

}
