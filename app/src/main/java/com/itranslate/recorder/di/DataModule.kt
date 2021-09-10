package com.itranslate.recorder.di

import com.itranslate.recorder.data.local.sources.LocalRepository
import com.itranslate.recorder.data.local.sources.LocalRepositoryImpl
import com.itranslate.recorder.data.remote.sources.RemoteRepository
import com.itranslate.recorder.data.remote.sources.RemoteRepositoryImpl
import org.koin.dsl.module

val dataModule = module {
    single<LocalRepository> { LocalRepositoryImpl(get()) }
    single<RemoteRepository> { RemoteRepositoryImpl() }
}