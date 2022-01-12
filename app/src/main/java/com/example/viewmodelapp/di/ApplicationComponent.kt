package com.example.viewmodelapp.di

import com.fasterxml.jackson.databind.ObjectMapper
import dagger.Component
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
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
    fun okHttpClient(): OkHttpClient
}

@Module
open class ApplicationModule(private val baseUrl: String) {
    @Singleton
    @Provides
    open fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
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


