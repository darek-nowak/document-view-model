package com.example.viewmodelapp

import com.example.viewmodelapp.di.DaggerApplicationComponent
import com.example.viewmodelapp.di.TestApplicationModule

class TestDocumentApplication : DocumentApplication() {

    override fun createApplicationComponent() {
        applicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(TestApplicationModule())
            .build()
    }
}