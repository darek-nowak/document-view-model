package com.example.viewmodelapp

import androidx.test.espresso.IdlingRegistry
import com.example.viewmodelapp.di.ApplicationModule
import com.example.viewmodelapp.di.DaggerApplicationComponent
import com.example.viewmodelapp.di.TestDispatchersModule
import com.example.viewmodelapp.rules.MOCK_WEBSERVER_PORT
import com.jakewharton.espresso.OkHttp3IdlingResource

class TestViewModelApplication : ViewModelApplication() {

    override fun onCreate() {
        super.onCreate()
        registerIdlingResources()
    }

    override fun createApplicationComponent() {
        applicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule("http://localhost:$MOCK_WEBSERVER_PORT"))
            .dispatchersModule(TestDispatchersModule())
            .build()
    }

    private fun registerIdlingResources() {
        val okHttpIdlingResource = OkHttp3IdlingResource.create("OkHttp", applicationComponent.okHttpClient())
        IdlingRegistry.getInstance().register(
            okHttpIdlingResource
        )
    }
}