package com.example.viewmodelapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.viewmodelapp.data.CvDocumentInfo
import com.example.viewmodelapp.data.DocumentListsInteractor
import com.example.viewmodelapp.data.Result
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import javax.inject.Inject

class DocumentViewModel(
    private val docsListInteractor: DocumentListsInteractor
): ViewModel() {
    private var disposable = Disposables.disposed()

    private val _state: MutableLiveData<DocumentsList> = MutableLiveData(DocumentsList.InProgress)
    val state: LiveData<DocumentsList> = _state

    init {
        setValue(DocumentsList.InProgress)
        //fetchDocumentsListData()
    }

    private fun fetchDocumentsListData() {
        disposable = docsListInteractor.getCvDocumentsList()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onStartLoadingList() }
            .subscribe { result ->
                when (result) {
                    is Result.Success -> onData(result.data)
                    Result.Error -> onError()
                }
            }
    }

    fun click() {
        fetchDocumentsListData()
    }

    private fun onStartLoadingList() {
        setValue(DocumentsList.InProgress)
    }

    private fun onData(data: List<CvDocumentInfo>) {
        setValue(DocumentsList.Success(data))
    }

    private fun onError() {
        setValue(DocumentsList.Error)
    }

    private fun setValue(value: DocumentsList) {
        _state.value = value
    }
}

sealed class DocumentsList {
    data class Success(val data: List<CvDocumentInfo>): DocumentsList()
    object Error: DocumentsList()
    object InProgress: DocumentsList()
}