package com.example.viewmodelapp.di

import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Singleton

@Module
class TestApplicationModule : ApplicationModule(){
    @Singleton
    @Provides
   override fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("http://localhost:8080")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(JacksonConverterFactory.create())
        .build()
}


