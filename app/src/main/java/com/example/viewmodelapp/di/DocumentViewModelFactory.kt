package com.example.viewmodelapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.viewmodelapp.viewmodel.DocumentViewModel
import com.example.viewmodelapp.data.DocumentInteractor
import com.example.viewmodelapp.data.DocumentListsInteractor
import javax.inject.Inject

// ViewModelProvider.NewInstanceFactory - bare bones factory
// ViewModelProvider.AndroidViewModelFactory - can supply view model with app context

class DocumentViewModelFactory @Inject constructor(
    val documentsInteractor: DocumentListsInteractor,
    val detailsInteractor: DocumentInteractor
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DocumentViewModel(documentsInteractor, detailsInteractor) as T
    }
}