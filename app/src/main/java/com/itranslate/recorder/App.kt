package com.itranslate.recorder

import androidx.multidex.MultiDexApplication
import com.itranslate.recorder.di.dataModule
import com.itranslate.recorder.di.dbModule
import com.itranslate.recorder.di.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                uiModule,
                dbModule,
                dataModule
            )
        }
    }

}