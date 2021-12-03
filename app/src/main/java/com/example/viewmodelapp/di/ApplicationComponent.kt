package com.example.viewmodelapp.di

import com.fasterxml.jackson.databind.ObjectMapper
import dagger.Component
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
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
open class ApplicationModule(private val baseUrl: String) {
    @Singleton
    @Provides
    open fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(JacksonConverterFactory.create())
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideJacksonObjectMapper() = ObjectMapper()

    @Singleton
    @Provides
    fun provideOkHttpClient() = OkHttpClient()
}


