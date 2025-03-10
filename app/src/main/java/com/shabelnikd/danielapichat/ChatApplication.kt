package com.shabelnikd.danielapichat

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ChatApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ChatApplication)
            modules(appModule, viewModelModule)
        }
    }
}