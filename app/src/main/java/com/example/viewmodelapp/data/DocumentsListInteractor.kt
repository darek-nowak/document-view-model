package com.example.viewmodelapp.data

import android.os.Parcelable
import android.util.Log
import com.example.viewmodelapp.di.DocumentActivityScope
import io.reactivex.Single
import io.reactivex.disposables.Disposables
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.parcel.Parcelize
import javax.inject.Inject

@DocumentActivityScope
class DocumentListsInteractor @Inject constructor(
    private val docRepository: GithubDocRepository
) {
    private val subject = BehaviorSubject.create<Result<List<CvDocumentInfo>>>()
    private var disposable = Disposables.disposed()

    fun getCvDocumentsList(): Single<Result<List<CvDocumentInfo>>> {
        return subject.doOnSubscribe {
            if (!subject.hasValue()) subscribeToSource()
        }
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
                { _ -> subject.onNext(Result.Error) }
            )
    }

    private fun String.toApplicantName() =
        removeSuffix(".json")
            .split("_")
            .joinToString(" ") { it.capitalize() }
}

@Parcelize
data class CvDocumentInfo(val filename: String, val name: String) : Parcelable

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    object Error : Result<Nothing>()
}