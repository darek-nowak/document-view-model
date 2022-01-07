package com.example.viewmodelapp.data

import io.reactivex.Single
import io.reactivex.disposables.Disposables
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber
import javax.inject.Inject

class DocumentInteractor @Inject constructor(
    private val docRepository: GithubDocRepository
) {
    private val subject = BehaviorSubject.create<Result<List<DocumentDisplayItem>>>()
    private var disposable = Disposables.disposed()

    fun getCvDocument(filename: String): Single<Result<List<DocumentDisplayItem>>> {
        disposable.dispose()
        disposable = docRepository.fetchDocument(filename)
            .map { cvData -> cvData.toDocumentDisplayItems() }
            .subscribe(
                { data -> subject.onNext(Result.Success(data)) },
                { e ->  subject.onNext(Result.Error)
                    Timber.d( e) }
            )
        return subject.firstOrError()
    }

    // sandbox
    fun getCvDocument2(filename: String): Single<Result<List<DocumentDisplayItem>>> {
        disposable.dispose()
        disposable = docRepository.fetchDocument(filename)
            .map { cvData -> cvData.toDocumentDisplayItems() }
            //.onErrorResumeNext { Single.just(Result.Error) }
            .subscribe(
                { data -> subject.onNext(Result.Success(data)) },
                { e ->  subject.onNext(Result.Error)
                    Timber.d( e) }
            )
        return subject.firstOrError()
    }
}
