package com.example.viewmodelapp

import com.example.viewmodelapp.di.ApplicationModule
import com.example.viewmodelapp.di.DaggerApplicationComponent

class TestDocumentApplication : DocumentApplication() {

    override fun createApplicationComponent() {
        applicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule("http://localhost:8080"))
            .build()
    }
}