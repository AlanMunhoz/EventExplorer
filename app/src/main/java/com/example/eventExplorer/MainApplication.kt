package com.example.eventExplorer

import android.app.Application
import com.example.eventExplorer.di.dataModule
import com.example.eventExplorer.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            modules(listOf(domainModule, dataModule))
        }
    }
}