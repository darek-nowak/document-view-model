package com.example.viewmodelapp

import android.app.Application
import com.example.viewmodelapp.di.ApplicationComponent
import com.example.viewmodelapp.di.DaggerApplicationComponent

class DocumentApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.create()

    }

    companion object {
        lateinit var applicationComponent: ApplicationComponent
    }
}