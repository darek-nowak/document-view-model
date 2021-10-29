package com.example.viewmodelapp.viewmodel

import androidx.lifecycle.Observer
import com.example.viewmodelapp.DocumentViewModel
import com.example.viewmodelapp.DocumentsList
import com.example.viewmodelapp.data.CvDocumentInfo
import com.example.viewmodelapp.data.DocumentListsInteractor
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import io.reactivex.Single
import org.junit.jupiter.api.Test
import com.example.viewmodelapp.data.Result
import com.example.viewmodelapp.utils.InstantTaskExecutorExtension
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
internal class DocumentViewModelShould {
    private val interactor: DocumentListsInteractor = mock()
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
        given(interactor.getCvDocumentsList()).willReturn(
            Single.just(Result.Success(SAMPLE_CV_DOCS_LIST))
        )

        viewModel.click()

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
        given(interactor.getCvDocumentsList()).willReturn(
            Single.just(Result.Error)
        )

        viewModel.click()

        verify(stateObserver).onChanged(
            DocumentsList.Error
        )
    }

    private companion object {
        val SAMPLE_CV_DOCS_LIST = listOf(CvDocumentInfo("sir_richard.json", "Sir Richard"))
    }
}