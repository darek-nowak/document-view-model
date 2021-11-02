package com.example.viewmodelapp.viewmodel

import androidx.lifecycle.Observer
import com.example.viewmodelapp.DocumentViewModel
import com.example.viewmodelapp.DocumentsList
import com.example.viewmodelapp.data.CvDocumentInfo
import com.example.viewmodelapp.data.DocumentListsInteractor
import com.example.viewmodelapp.data.Result
import com.example.viewmodelapp.utils.InstantTaskExecutorExtension
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.SingleSubject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
internal class DocumentViewModelShould {
    private val documentsSubject =  SingleSubject.create<Result<List<CvDocumentInfo>>>()
    private val interactor: DocumentListsInteractor = mock {
        on { it.getCvDocumentsList() }.thenReturn(documentsSubject)
    }
    private val stateObserver: Observer<DocumentsList> = mock()

    private lateinit var viewModel: DocumentViewModel

    @BeforeEach
    fun setUp() {
        // create Rule or Extension
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        viewModel = DocumentViewModel(interactor)
            .apply {
                state.observeForever(stateObserver)
            }
    }

    @Test
    fun `update view on successful of document list loading`() {
        documentsSubject.onSuccess(Result.Success(SAMPLE_CV_DOCS_LIST))

        verify(stateObserver).onChanged(
            DocumentsList.Success(SAMPLE_CV_DOCS_LIST)
        )
    }

    @Test
    fun `update view on progress of document list loading`() {
        verify(stateObserver).onChanged(
            DocumentsList.InProgress
        )
    }

    @Test
    fun `update view on failure of document list loading`() {
        documentsSubject.onSuccess(Result.Error)

        verify(stateObserver).onChanged(
            DocumentsList.Error
        )
    }

    private companion object {
        val SAMPLE_CV_DOCS_LIST = listOf(CvDocumentInfo("sir_richard.json", "Sir Richard"))
    }
}