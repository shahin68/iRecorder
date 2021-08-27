package com.itranslate.recorder.di

import com.itranslate.recorder.data.Repository
import com.itranslate.recorder.data.RepositoryImpl
import com.itranslate.recorder.data.local.sources.LocalRepository
import com.itranslate.recorder.data.local.sources.LocalRepositoryImpl
import com.itranslate.recorder.data.remote.sources.RemoteRepository
import com.itranslate.recorder.data.remote.sources.RemoteRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Singleton
    @Binds
    abstract fun bindLocalRepository(
        localRepositoryImpl: LocalRepositoryImpl
    ): LocalRepository

    @Singleton
    @Binds
    abstract fun bindRemoteRepository(
        remoteRepositoryImpl: RemoteRepositoryImpl
    ): RemoteRepository

    @Singleton
    @Binds
    abstract fun bindRepository(
        repositoryImpl: RepositoryImpl
    ): Repository

}