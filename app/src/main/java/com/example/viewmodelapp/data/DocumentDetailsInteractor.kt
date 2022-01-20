package com.example.viewmodelapp.data

import com.example.viewmodelapp.CoroutineDispatchersProvider
import javax.inject.Inject

class DocumentDetailsInteractor @Inject constructor(
    private val docRepository: GithubDocRepository,
    private val dispatchersProvider: CoroutineDispatchersProvider
) {
    suspend fun getCvDocument(filename: String): Result<List<DocumentDisplayItem>> =
        asResult(dispatchersProvider.io()) {
            docRepository.fetchDocument(filename)
                .toDocumentDisplayItems()
        }
}
