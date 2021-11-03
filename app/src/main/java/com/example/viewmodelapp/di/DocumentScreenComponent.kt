package com.example.viewmodelapp.di

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import com.example.viewmodelapp.DetailsFragment
import com.example.viewmodelapp.presentation.DocumentsFragment
import com.example.viewmodelapp.DocumentApplication
import com.example.viewmodelapp.presentation.MainActivity
import com.example.viewmodelapp.data.GitHubApi
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import retrofit2.Retrofit
import javax.inject.Scope

@DocumentActivityScope
@Subcomponent
interface DocumentScreenComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): DocumentScreenComponent
    }

    fun inject(activity: MainActivity)
    fun inject(documentFragment: DetailsFragment)
    fun inject(documentsListFragment: DocumentsFragment)
}

class DocumentScreenComponentHolder : ViewModel() {
    val component = DocumentApplication.applicationComponent
        .documentScreenComponent().create()

    companion object {
        fun getComponent(activity: ComponentActivity): DocumentScreenComponent {
            val holder: DocumentScreenComponentHolder by activity.viewModels()
            return holder.component
        }
    }
}

@Module(subcomponents = [ DocumentScreenComponent::class ])
class DocumentScreenModule {
    @Provides
    fun provideGitHubApi(retrofit: Retrofit): GitHubApi = retrofit.create(GitHubApi::class.java)
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class DocumentActivityScope
