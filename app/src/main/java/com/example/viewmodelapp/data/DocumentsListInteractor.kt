package com.example.viewmodelapp.data

import android.os.Parcelable
import io.reactivex.Single
import io.reactivex.disposables.Disposables
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.parcel.Parcelize
import timber.log.Timber
import javax.inject.Inject

class DocumentListsInteractor @Inject constructor(
    private val docRepository: GithubDocRepository
) {
    private val subject = BehaviorSubject.create<Result<List<CvDocumentInfo>>>()
    private var disposable = Disposables.disposed()

    fun getCvDocumentsList(): Single<Result<List<CvDocumentInfo>>> {
        return subject.doOnSubscribe { subscribeToSource() }
            .doOnDispose { disposable.dispose() }
            .firstOrError()
    }

    private fun subscribeToSource() {
        disposable = docRepository.fetchDocumentsList()
            .map { files ->
                files.map {
                    CvDocumentInfo(
                        name = it.filename.toApplicantName(),
                        filename = it.filename
                    )
                }
            }
            .subscribe(
                { data -> subject.onNext(Result.Success(data)) },
                { e -> subject.onNext(Result.Error)
                  Timber.e(e) }
            )
    }

    private fun String.toApplicantName() =
        removeSuffix(".json")
            .split("_")
            .joinToString(" ") { it.capitalize() }
}

@Parcelize
data class CvDocumentInfo(val filename: String, val name: String) : Parcelable
