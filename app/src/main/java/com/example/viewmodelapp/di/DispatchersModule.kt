package com.example.viewmodelapp.di

import com.example.viewmodelapp.CoroutineDispatchersProvider
import com.example.viewmodelapp.DefaultCoroutineDispatchersProvider
import dagger.Module
import dagger.Provides

@Module
open class DispatchersModule {
    @Provides
    open fun provideCoroutineDispatchersProvider(): CoroutineDispatchersProvider = DefaultCoroutineDispatchersProvider()
}