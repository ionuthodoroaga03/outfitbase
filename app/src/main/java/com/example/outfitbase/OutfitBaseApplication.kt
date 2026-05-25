package com.example.outfitbase

import android.app.Application
import com.example.outfitbase.util.FileLoggingTree
import timber.log.Timber

class OutfitBaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        Timber.plant(FileLoggingTree(filesDir))
    }
}
