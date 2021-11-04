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

    private val _documents: MutableLiveData<DocumentsState> = MutableLiveData()
    val documents: LiveData<DocumentsState> = _documents

    private val _details: MutableLiveData<DetailsState> = MutableLiveData()
    val details: LiveData<DetailsState> = _details

    fun fetchDocuments() {
        disposable = documentsInteractor.getCvDocumentsList()
            .doOnSubscribe { setDocumentsValue(DocumentsState.InProgress) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                when (result) {
                    is Result.Success -> setDocumentsValue(DocumentsState.Documents(result.data))
                    Result.Error -> setDocumentsValue(DocumentsState.Error)
                }
            }
    }

    private fun setDocumentsValue(value: DocumentsState) {
        _documents.value = value
    }

    private fun setDetailsValue(value: DetailsState) {
        _details.value = value
    }

    fun fetchDetails(filename: String) {
        // if (_details.value is DetailsState.Details && filename == _details.value) {
        //     return
        // }
        disposable = detailsInteractor.getCvDocument(filename)
            .doOnSubscribe { setDetailsValue(DetailsState.InProgress) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                when (result) {
                    is Result.Success -> setDetailsValue(DetailsState.Details(result.data))
                    Result.Error -> setDetailsValue(DetailsState.Error)
                }
            }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
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