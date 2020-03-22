package com.example.starwarskamino

import android.app.Application
import com.example.starwarskamino.general.DebugTree
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        // Enable logging only for the DEBUG configuration. This way the RELEASE config will show no logs.
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}