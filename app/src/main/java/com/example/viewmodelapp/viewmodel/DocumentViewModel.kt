package com.example.viewmodelapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.viewmodelapp.data.*
import kotlinx.coroutines.launch

class DocumentViewModel(
    private val documentsInteractor: DocumentListsInteractor,
    private val detailsInteractor: DocumentInteractor
) : ViewModel() {
    private val _documents: MutableLiveData<DocumentsState> = MutableLiveData()
    val documents: LiveData<DocumentsState> = _documents

    private val _details: MutableLiveData<DetailsState> = MutableLiveData()
    val details: LiveData<DetailsState> = _details

    private var lastFilename = ""

    fun fetchDocuments() {
        if (_documents.value is DocumentsState.Documents) { return }

        viewModelScope.launch {
            setDocumentsValue(DocumentsState.InProgress)
            when(val result = documentsInteractor.getCvDocumentsList()) {
                is Result.Success -> setDocumentsValue(DocumentsState.Documents(result.data))
                Result.Error -> setDocumentsValue(DocumentsState.Error)
            }
        }
    }

    private fun setDocumentsValue(value: DocumentsState) {
        _documents.value = value
    }

    fun fetchDetails(filename: String) {
         if (filename == lastFilename) { return }

        lastFilename = filename
        viewModelScope.launch {
            setDetailsValue(DetailsState.InProgress)
            when(val result = detailsInteractor.getCvDocument(filename)) {
                is Result.Success -> setDetailsValue(DetailsState.Details(result.data))
                Result.Error -> setDetailsValue(DetailsState.Error)
            }
        }
    }

    private fun setDetailsValue(value: DetailsState) {
        _details.value = value
    }
}

sealed class DocumentsState {
    data class Documents(val data: List<CvDocumentInfo>) : DocumentsState()
    object Error : DocumentsState()
    object InProgress : DocumentsState()
}

sealed class DetailsState {
    data class Details(val data: List<DocumentDisplayItem>) : DetailsState()
    object Error : DetailsState()
    object InProgress : DetailsState()
}