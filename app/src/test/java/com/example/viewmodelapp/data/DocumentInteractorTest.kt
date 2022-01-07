package com.example.viewmodelapp.data

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import io.reactivex.Single
import io.reactivex.subjects.SingleSubject
import org.junit.jupiter.api.Test

internal class DocumentInteractorTest {
    private val fetchDocsSubject = SingleSubject.create<CvData>()
    private val docRepository = mock<GithubDocRepository>()
//    {
//        on { it.fetchDocument(any()) }.thenReturn(fetchDocsSubject)
//    }

    private val interactor = DocumentInteractor(docRepository)

    @Test
    fun `return Result Error on doc fetching error`() {
        given(docRepository.fetchDocument("filename")).willReturn(
            Single.error(Throwable("error"))
        )

        fetchDocsSubject.onError(Throwable("error"))

        val test = interactor.getCvDocument("filename").test()

        test.assertValue(Result.Error)
    }

    @Test
    fun `provide result with list of document content mapped from CvData`() {
        given(docRepository.fetchDocument("filename")).willReturn(
            Single.just(CV_DATA_MODEL)
        )

        val tested = interactor.getCvDocument("filename").test()

        tested.assertValue(
            Result.Success(
                listOf(
                    DocumentDisplayItem.ExtraBigItem("Rafal Kowalski"),
                    DocumentDisplayItem.ExtraBigItem("Senior Android Developer"),
                    DocumentDisplayItem.Item("Android developer experienced in RxJava programming in fintech sector"),
                    DocumentDisplayItem.Header("Professional Experience"),
                    DocumentDisplayItem.ParagraphItem("08/2016 - till now"),
                    DocumentDisplayItem.BigItem("Facebook, US"),
                    DocumentDisplayItem.BigItem("Sr Android Developer"),
                    DocumentDisplayItem.Item("• Designing Custom Views"),
                    DocumentDisplayItem.Item("• Grooming Planning tasks"),
                    DocumentDisplayItem.Item("• Application development")
                )
            )
        )
    }

    private companion object {
        val CV_DATA_MODEL = CvData(
            applicant = "Rafal Kowalski",
            currentRole = "Senior Android Developer",
            description = "Android developer experienced in RxJava programming in fintech sector",
            experience = listOf(
                ExperienceItem(
                    startDate = "08/2016",
                    endDate = "",
                    company = "Facebook, US",
                    role = "Sr Android Developer",
                    responsibilities = listOf(
                        "Designing Custom Views",
                        "Grooming Planning tasks",
                        "Application development"
                    )
                )
            )
        )
    }
}