package com.example.viewmodelapp.di

import com.example.viewmodelapp.CoroutineDispatchersProvider
import com.example.viewmodelapp.DefaultCoroutineDispatchersProvider
import com.example.viewmodelapp.TestCoroutineDispatchersProvider
import dagger.Module
import dagger.Provides

@Module
class TestDispatchersModule: DispatchersModule() {
    @Provides
    override fun provideCoroutineDispatchersProvider(): CoroutineDispatchersProvider = TestCoroutineDispatchersProvider()
}