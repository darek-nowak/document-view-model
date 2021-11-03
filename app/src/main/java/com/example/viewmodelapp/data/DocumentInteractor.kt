package com.example.viewmodelapp.data

import android.os.Parcelable
import io.reactivex.Single
import io.reactivex.disposables.Disposables
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.parcel.Parcelize
import javax.inject.Inject

class DocumentInteractor @Inject constructor(
    private val docRepository: GithubDocRepository
) {
    private val subject = BehaviorSubject.create<Result<List<DocumentDisplayItem>>>()
    private var disposable = Disposables.disposed()
    private var lastFilename: String? = null

    fun getCvDocument(filename: String): Single<Result<List<DocumentDisplayItem>>> {

        return subject.doOnSubscribe {
            if (!subject.hasValue() || filename != lastFilename) {
                subject.onNext(emptyResult())
                lastFilename = filename
                subscribeToSource(filename)
            }
        }
            .filter { !it.isEmptyResult() }
            .doOnDispose { disposable.dispose() }
            .firstOrError()
    }

    private fun emptyResult(): Result<List<DocumentDisplayItem>> = Result.Success(emptyList())
    private fun Result<List<DocumentDisplayItem>>.isEmptyResult(): Boolean =
        this is Result.Success && data.isEmpty()


    private fun subscribeToSource(filename: String) {
        disposable = docRepository.fetchDocument(filename)
            .map { cvData -> cvData.toDocumentDisplayItems() }
            .subscribe(
                { data -> subject.onNext(Result.Success(data)) },
                { _ -> subject.onNext(Result.Error) }
            )
    }

    private fun CvData.toDocumentDisplayItems() = mutableListOf(
        DocumentDisplayItem.ExtraBigItem(applicant),
        DocumentDisplayItem.ExtraBigItem(currentRole),
        DocumentDisplayItem.Item(description),
        DocumentDisplayItem.Header("Professional Experience")
    ).apply {
        experience.forEach { item ->
            add(DocumentDisplayItem.ParagraphItem(item.startDate + " - " + item.endDate.addIfEmpty("till now")))
            add(DocumentDisplayItem.BigItem(item.company))
            add(DocumentDisplayItem.BigItem(item.role))
            item.responsibilities.forEach {
                add(DocumentDisplayItem.Item("• $it"))
            }
        }
    }

    private fun String.addIfEmpty(name: String) = if (this.isBlank()) name else this
}

sealed class DocumentDisplayItem: Parcelable {
    abstract val name: String

    @Parcelize
    data class ExtraBigItem(override val name: String) : DocumentDisplayItem()
    @Parcelize
    data class BigItem(override val name: String) : DocumentDisplayItem()
    @Parcelize
    data class ParagraphItem(override val name: String) : DocumentDisplayItem()
    @Parcelize
    data class Header(override val name: String) : DocumentDisplayItem()
    @Parcelize
    data class Item(override val name: String) : DocumentDisplayItem()
}