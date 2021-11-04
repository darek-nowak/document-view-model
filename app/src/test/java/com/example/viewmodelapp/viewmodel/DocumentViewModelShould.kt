package com.example.viewmodelapp.viewmodel

import androidx.lifecycle.Observer
import com.example.viewmodelapp.DetailsState
import com.example.viewmodelapp.DocumentViewModel
import com.example.viewmodelapp.DocumentsState
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
    private val documentsInteractor: DocumentListsInteractor = mock {
        on { it.getCvDocumentsList() }.thenReturn(documentsSubject)
    }
    private val detailsInteractor: DocumentInteractor = mock()
    private val documentsObserver: Observer<DocumentsState> = mock()
    private val detailsObserver: Observer<DetailsState> = mock()

    private lateinit var viewModel: DocumentViewModel

    @BeforeEach
    fun setUp() {
        // create Rule or Extension
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        viewModel = DocumentViewModel(documentsInteractor, detailsInteractor)
            .apply {
                documents.observeForever(documentsObserver)
                details.observeForever(detailsObserver)
            }
    }

    @Test
    fun `update view on success of document list loading`() {
        viewModel.fetchDocuments()

        documentsSubject.onSuccess(Result.Success(DOCUMENTS_LIST))

        verify(documentsObserver).onChanged(
            DocumentsState.Documents(DOCUMENTS_LIST)
        )
    }

    @Test
    fun `update view on progress of document list loading`() {
        viewModel.fetchDocuments()

        verify(documentsObserver).onChanged(
            DocumentsState.InProgress
        )
    }

    @Test
    fun `update view on failure of document list loading`() {
        viewModel.fetchDocuments()

        documentsSubject.onSuccess(Result.Error)

        verify(documentsObserver).onChanged(
            DocumentsState.Error
        )
    }

    @Test
    fun `update view on success of document details loading`() {
        given(detailsInteractor.getCvDocument(DOCUMENT_INFO.filename))
            .willReturn(Single.just(Result.Success(DOCUMENT_DETAILS)))

        viewModel.fetchDetails(DOCUMENT_INFO.filename)

        verify(detailsObserver).onChanged(
            DetailsState.Details(DOCUMENT_DETAILS)
        )
    }

    @Test
    fun `update view on progress of document details loading`() {
        given(detailsInteractor.getCvDocument(DOCUMENT_INFO.filename))
            .willReturn(Single.just(Result.Success(DOCUMENT_DETAILS)))
        reset(documentsObserver)

        viewModel.fetchDetails(DOCUMENT_INFO.filename)

        verify(detailsObserver).onChanged(DetailsState.InProgress)
    }

    @Test
    fun `update view on error of document details loading`() {
        given(detailsInteractor.getCvDocument(DOCUMENT_INFO.filename))
            .willReturn(Single.just(Result.Error))

        viewModel.fetchDetails(DOCUMENT_INFO.filename)

        verify(detailsObserver).onChanged(DetailsState.Error)
    }

    private companion object {
        val DOCUMENTS_LIST = listOf(CvDocumentInfo("sir_richard.json", "Sir Richard"))
        val DOCUMENT_INFO = CvDocumentInfo("Sir_Richard.json", "Sir Richard")
        val DOCUMENT_DETAILS = listOf(DocumentDisplayItem.ExtraBigItem("Sir Richard"))
    }
}