package com.gachateam.wacawiraga

import android.app.Application
import timber.log.Timber
import timber.log.Timber.DebugTree

class WacaWiragaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}