package com.example.starwarskamino

import com.example.starwarskamino.di.DaggerAppComponent
import com.example.starwarskamino.general.DebugTree
import dagger.android.DaggerApplication
import timber.log.Timber

class App : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()

        // Enable logging only for the DEBUG configuration. This way the RELEASE config will show no logs.
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }

    private val applicationInjector = DaggerAppComponent.factory().create(this)

    override fun applicationInjector() = applicationInjector
}