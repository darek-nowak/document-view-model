package com.example.viewmodelapp

import android.app.Application
import com.example.viewmodelapp.di.ApplicationComponent
import com.example.viewmodelapp.di.ApplicationModule
import com.example.viewmodelapp.di.DaggerApplicationComponent

open class DocumentApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        createApplicationComponent()
    }

    open fun createApplicationComponent() {
        applicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule("https://api.github.com/"))
            .build()
    }

    companion object {
        lateinit var applicationComponent: ApplicationComponent
    }
}