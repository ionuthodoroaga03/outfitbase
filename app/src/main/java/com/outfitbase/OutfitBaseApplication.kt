package com.outfitbase

import android.app.Application
import com.outfitbase.di.AppContainer
import com.outfitbase.di.DefaultAppContainer
import com.outfitbase.util.FileLoggingTree
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
