package com.example.viewmodelapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.viewmodelapp.data.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables

class DocumentViewModel(
    private val documentsInteractor: DocumentListsInteractor,
    private val detailsInteractor: DocumentInteractor
) : ViewModel() {
    private var disposable = Disposables.disposed()

    private val _state: MutableLiveData<DocumentState> = MutableLiveData(DocumentState.InProgress)
    val state: LiveData<DocumentState> = _state

    init {
        fetchDocumentsListData()
    }

    private fun fetchDocumentsListData() {
        disposable = documentsInteractor.getCvDocumentsList()
            .doOnSubscribe { onStartLoadingList() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                when (result) {
                    is Result.Success -> onData(result.data)
                    Result.Error -> onError()
                }
            }
    }

    private fun onData(data: List<CvDocumentInfo>) {
        setValue(DocumentState.Documents(data))
    }

    private fun setValue(value: DocumentState) {
        _state.value = value
    }

    fun selectDocument(selectedDocument: CvDocumentInfo) {
        fetchDocumentDetails(selectedDocument.filename)
    }

    private fun fetchDocumentDetails(filename: String) {
        disposable = detailsInteractor.getCvDocument(filename)
            .doOnSubscribe { onStartLoadingList() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                when (result) {
                    is Result.Success -> onDetailsData(result.data)
                    Result.Error -> onError()
                }
            }
    }

    private fun onDetailsData(data: List<DocumentDisplayItem>) {
        setValue(DocumentState.Details(data))
    }

    private fun onStartLoadingList() {
        setValue(DocumentState.InProgress)
    }

    private fun onError() {
        setValue(DocumentState.Error)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}

sealed class DocumentState {
    data class Documents(val data: List<CvDocumentInfo>) : DocumentState()
    data class Details(val data: List<DocumentDisplayItem>) : DocumentState()
    object Error : DocumentState()
    object InProgress : DocumentState()
}