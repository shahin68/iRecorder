package com.itranslate.recorder.data

import com.itranslate.recorder.data.local.sources.LocalRepository
import com.itranslate.recorder.data.remote.sources.RemoteRepository
import javax.inject.Inject

/**
 * Class representing a single data source to all other parts of the project
 *
 * Provides access to [LocalRepository] & [RemoteRepository] indirectly to other classes
 * including View Models
 *
 */
class RepositoryImpl @Inject constructor(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository
) : Repository {

}