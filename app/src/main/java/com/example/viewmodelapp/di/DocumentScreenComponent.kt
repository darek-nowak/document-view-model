package com.example.viewmodelapp.di

import com.example.viewmodelapp.data.GitHubApi
import com.example.viewmodelapp.presentation.DocumentsListFragment
import com.example.viewmodelapp.presentation.MainActivity
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import retrofit2.Retrofit

@Subcomponent
interface DocumentScreenComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): DocumentScreenComponent
    }

    fun inject(activity: MainActivity)
    fun inject(documentsListFragment: DocumentsListFragment)
}

@Module(subcomponents = [ DocumentScreenComponent::class ])
class DocumentScreenModule {
    @Provides
    fun provideGitHubApi(retrofit: Retrofit): GitHubApi = retrofit.create(GitHubApi::class.java)
}
