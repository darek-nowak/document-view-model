package com.example.viewmodelapp

import android.app.Application
import android.app.Instrumentation
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class ViewModelInstrumentationRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        return Instrumentation.newApplication(TestDocumentApplication::class.java, context)
    }
}