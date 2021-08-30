package com.itranslate.recorder.di

import com.itranslate.recorder.ui.fragments.home.HomeViewModel
import com.itranslate.recorder.ui.fragments.recordings.RecordingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { RecordingsViewModel(get()) }
}