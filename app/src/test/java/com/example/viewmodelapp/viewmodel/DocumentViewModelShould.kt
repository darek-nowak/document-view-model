package com.example.viewmodelapp.viewmodel

import androidx.lifecycle.Observer
import com.example.viewmodelapp.DocumentViewModel
import com.example.viewmodelapp.DocumentState
import com.example.viewmodelapp.data.*
import com.example.viewmodelapp.utils.InstantTaskExecutorExtension
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.SingleSubject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
internal class DocumentViewModelShould {
    private val documentsSubject =  SingleSubject.create<Result<List<CvDocumentInfo>>>()
    private val detailsSubject = SingleSubject.create<Result<List<DocumentDisplayItem>>>()
    private val documentsInteractor: DocumentListsInteractor = mock {
        on { it.getCvDocumentsList() }.thenReturn(documentsSubject)
    }
    private val detailsInteractor: DocumentInteractor = mock()
    private val stateObserver: Observer<DocumentState> = mock()

    private lateinit var viewModel: DocumentViewModel

    @BeforeEach
    fun setUp() {
        // create Rule or Extension
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        viewModel = DocumentViewModel(documentsInteractor, detailsInteractor)
            .apply {
                state.observeForever(stateObserver)
            }
    }

    @Test
    fun `update view on successful of document list loading`() {
        documentsSubject.onSuccess(Result.Success(DOCUMENTS_LIST))

        verify(stateObserver).onChanged(
            DocumentState.Documents(DOCUMENTS_LIST)
        )
    }

    @Test
    fun `update view on progress of document list loading`() {
        verify(stateObserver).onChanged(
            DocumentState.InProgress
        )
    }

    @Test
    fun `update view on failure of document list loading`() {
        documentsSubject.onSuccess(Result.Error)

        verify(stateObserver).onChanged(
            DocumentState.Error
        )
    }

    @Test
    fun `update view on success of document details loading`() {
        given(detailsInteractor.getCvDocument(DOCUMENT_INFO.filename))
            .willReturn(Single.just(Result.Success(DOCUMENT_DETAILS)))

        viewModel.selectDocument(DOCUMENT_INFO)

        verify(stateObserver).onChanged(
            DocumentState.Details(DOCUMENT_DETAILS)
        )
    }

    @Test
    fun `update view on progress of document details loading`() {
        given(detailsInteractor.getCvDocument(DOCUMENT_INFO.filename))
            .willReturn(Single.just(Result.Success(DOCUMENT_DETAILS)))
        reset(stateObserver)

        viewModel.selectDocument(DOCUMENT_INFO)

        verify(stateObserver).onChanged(DocumentState.InProgress)
    }

    @Test
    fun `update view on error of document details loading`() {
        given(detailsInteractor.getCvDocument(DOCUMENT_INFO.filename))
            .willReturn(Single.just(Result.Error))

        viewModel.selectDocument(DOCUMENT_INFO)

        verify(stateObserver).onChanged(DocumentState.Error)
    }

    private companion object {
        val DOCUMENTS_LIST = listOf(CvDocumentInfo("sir_richard.json", "Sir Richard"))
        val DOCUMENT_INFO = CvDocumentInfo("Sir_Richard.json", "Sir Richard")
        val DOCUMENT_DETAILS = listOf(DocumentDisplayItem.ExtraBigItem("Sir Richard"))
    }
}