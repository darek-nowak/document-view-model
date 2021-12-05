package com.example.viewmodelapp

import android.app.Application
import com.example.viewmodelapp.di.ApplicationComponent
import com.example.viewmodelapp.di.ApplicationModule
import com.example.viewmodelapp.di.DaggerApplicationComponent
import timber.log.Timber

open class ViewModelApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        createApplicationComponent()
        initializeTimer()
    }

    open fun createApplicationComponent() {
        applicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule("https://api.github.com/"))
            .build()
    }

    private fun initializeTimer() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    companion object {
        lateinit var applicationComponent: ApplicationComponent
    }
}