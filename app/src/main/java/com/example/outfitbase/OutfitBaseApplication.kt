package com.example.outfitbase

import android.app.Application
import com.example.outfitbase.di.AppContainer
import com.example.outfitbase.di.DefaultAppContainer
import com.example.outfitbase.util.FileLoggingTree
import timber.log.Timber

class OutfitBaseApplication : Application() {
    val appContainer: AppContainer by lazy {
        DefaultAppContainer(this)
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        Timber.plant(FileLoggingTree(filesDir))
    }
}
