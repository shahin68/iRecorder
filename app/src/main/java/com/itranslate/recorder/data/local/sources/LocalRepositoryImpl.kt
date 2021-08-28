package com.itranslate.recorder.data.local.sources

import com.itranslate.recorder.data.local.sources.db.records.RecordsDao
import javax.inject.Inject

/**
 * Local data source access point
 * Including access to all locally stored data, databases & devices storage
 *
 * implements [LocalRepository] properties
 *
 * @param recordsDao is an access point for records table through local database
 */
class LocalRepositoryImpl @Inject constructor(
    private val recordsDao: RecordsDao
): LocalRepository {



}