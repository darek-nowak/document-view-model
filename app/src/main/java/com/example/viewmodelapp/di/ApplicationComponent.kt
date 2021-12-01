package com.example.viewmodelapp.di

import com.fasterxml.jackson.databind.ObjectMapper
import dagger.Component
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        DocumentScreenModule::class
    ]
)
interface ApplicationComponent {
    fun documentScreenComponent(): DocumentScreenComponent.Factory
}

@Module
open class ApplicationModule {
    @Singleton
    @Provides
    open fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(JacksonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun provideJacksonObjectMapper() = ObjectMapper()
}


