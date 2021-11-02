package com.example.viewmodelapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.viewmodelapp.data.CvDocumentInfo
import com.example.viewmodelapp.data.DocumentListsInteractor
import com.example.viewmodelapp.data.Result
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DocumentViewModel(
    private val docsListInteractor: DocumentListsInteractor
): ViewModel() {
    private var disposable = Disposables.disposed()

    private val _state: MutableLiveData<DocumentsList> = MutableLiveData(DocumentsList.InProgress)
    val state: LiveData<DocumentsList> = _state

    init {
        fetchDocumentsListData()
    }

    private fun fetchDocumentsListData() {
        disposable = docsListInteractor.getCvDocumentsList()
            .doOnSubscribe { onStartLoadingList() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                when (result) {
                    is Result.Success -> onData(result.data)
                    Result.Error -> onError()
                }
            }
    }

    private fun onStartLoadingList() {
        Log.d("docs", "InProgress")
        setValue(DocumentsList.InProgress)
    }

    private fun onData(data: List<CvDocumentInfo>) {
        Log.d("docs", "onData")
        setValue(DocumentsList.Success(data))
    }

    private fun onError() {
        Log.d("docs", "onError")
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